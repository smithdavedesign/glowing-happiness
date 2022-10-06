package com.solidigm.core.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.solidigm.core.beans.KnowledgeArticle;
import com.solidigm.core.services.ProcessDynamicsArticles;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.microsoft.aad.msal4j.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Component(service = ProcessDynamicsArticles.class, immediate = true)
public class ProcessDynamicsArticlesImpl implements ProcessDynamicsArticles {

    /* The Constant logger */
    private static final Logger logger = LoggerFactory.getLogger(ProcessDynamicsArticlesImpl.class);

    @Reference
    private transient ResourceResolverFactory resolverFactory;

    @Override
    public String aquireToken(
    ) throws UnsupportedEncodingException {

        String authority = "https://login.microsoftonline.com/4bb44aac-867a-4be0-aa34-30794e8470dd/oauth2/v2.0/token";
        String clientId = "cb76104c-f2ae-4118-9a64-397aab69f9c0";
        String clientSecret = "cxxIW7y__z4lUV2.UHk6n7x27l-869J~gF";
        Set<String> SCOPE = Collections.singleton("https://nandqa.api.crm.dynamics.com/.default");
        IAuthenticationResult result = null;
        String accessToken = null;

        try {

            // This is the secret that is created in the Azure portal when registering the application
            IClientCredential credential = ClientCredentialFactory.createFromSecret(clientSecret);
            ConfidentialClientApplication cca =
                    ConfidentialClientApplication
                            .builder(clientId, credential)
                            .authority(authority)
                            .build();

            // Client credential requests will by default try to look for a valid token in the
            // in-memory token cache. If found, it will return this token. If a token is not found, or the
            // token is not valid, it will fall back to acquiring a token from the AAD service. Although
            // not recommended unless there is a reason for doing so, you can skip the cache lookup
            // by using .skipCache(true) in ClientCredentialParameters.
            ClientCredentialParameters parameters =
                    ClientCredentialParameters
                            .builder(SCOPE)
                            .skipCache(true)
                            .build();

            result = cca.acquireToken(parameters).join();
            accessToken = result.accessToken();

        } catch (Exception e) {
            logger.error("LoginException {}", e);
        }
        return accessToken;
    }

    public String fetchArticles(String accessToken){

        String articleJson = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet("https://nandqa.api.crm.dynamics.com/api/data/v9.2/knowledgearticles?$filter=(statuscode%20eq%207)");

            // add request headers
            request.addHeader("Authorization", "Bearer " + accessToken.trim());
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Accept", "application/json");

            CloseableHttpResponse response = httpClient.execute(request);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // return it as a String
                articleJson = EntityUtils.toString(entity);
            }

        }catch(Exception e){
            logger.error("LoginException {}", e);
        }
        return articleJson;
    }

    public List<KnowledgeArticle> parseKnowledgeArticleResponse(String articleJson)
            throws UnsupportedEncodingException {

        JSONObject result = new JSONObject();
        List<KnowledgeArticle> knowledgeArticleList = null;

        try {

            if(articleJson!=null) {
                Map<String, Object> param = new HashMap<>();
                param.put(ResourceResolverFactory.SUBSERVICE, "knowledgeArticlesServiceUser");
                ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param);
                //articleJson = new String(resolver.getResource("/var/dynamicarticles.json").adaptTo(InputStream.class).readAllBytes());

                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(articleJson, JsonObject.class);
                JsonArray knowledgeArticles = jsonObject.getAsJsonArray("value");

                knowledgeArticleList = new ArrayList<>();

                for (int i = 0; i < knowledgeArticles.size(); i++) {
                    KnowledgeArticle knowledgeArticle = new KnowledgeArticle();
                    JsonObject article = knowledgeArticles.get(i).getAsJsonObject();
                    knowledgeArticle.setArticleId(article.get("knowledgearticleid").getAsString());
                    knowledgeArticle.setTitle(article.get("title").getAsString());
                    knowledgeArticle.setDescription(article.get("description").getAsString());
                    knowledgeArticle.setContent(StringEscapeUtils.unescapeJava(article.get("content").getAsString().replaceAll("\"", "'")));
                    knowledgeArticle.setCreatedOn(article.get("createdon").getAsString());
                    knowledgeArticle.setModifiedOn(article.get("modifiedon").getAsString());

                    knowledgeArticleList.add(knowledgeArticle);
                }
            }
        } catch (Exception e) {
            logger.error("LoginException {}", e);
        }
        return knowledgeArticleList;
    }

    public String processKnowledgeArticleList(List<KnowledgeArticle> knowledgeArticleList)
            throws UnsupportedEncodingException, LoginException {

        /* Configure ResourceResolver object with the help of apache sling service user Mapper Amendment */
        /* Also create the systemuser configured in resource resolver setup with right permissions to access apps, etc and content folder */

        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, "knowledgeArticlesServiceUser");
        ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param);
        Session session = resolver.adaptTo(Session.class);

        try {
            String path = "/content/solidigm/language-masters/en/support";
            Resource resource = resolver.getResource(path);
            Node jcrContent = resource.adaptTo(Node.class);

            /*Delete article pages that no longer exist in dynamics */
            List<String> removeArticleNodeList = buildArticleNodeList(resource.adaptTo(Page.class));
            /* if (!removeArticleNodeList.isEmpty() && !knowledgeArticleList.isEmpty()) {
                for (int i = 0; i < removeArticleNodeList.size(); i++) {
                    for (KnowledgeArticle knowledgeArticle : knowledgeArticleList) {
                        if (removeArticleNodeList.get(i).equals(knowledgeArticle.getArticleId())) {
                            removeArticleNodeList.remove(i);
                        }
                    }
                }
            } */
            //TODO: uncommment above to not totally rewrite all articles
            if (!removeArticleNodeList.isEmpty()) {
                for (String articleNode : removeArticleNodeList) {
                    Node removeArticleNode = jcrContent.getNode(articleNode);
                    if (removeArticleNode != null) {
                        removeKnowledgeArticleNode(session, removeArticleNode);
                    }
                }
            }

            /*Add or modify article pages */
            for (KnowledgeArticle knowledgeArticle : knowledgeArticleList) {
                addKnowledgeArticleNode(resolverFactory, knowledgeArticle);
            }
            session.save();
            session.logout();
        } catch (Exception e) {
            logger.error("LoginException {}", e);
        }

        return "Success";
    }


    private String addKnowledgeArticleNode(ResourceResolverFactory resolverFactory, KnowledgeArticle knowledgeArticle) throws LoginException {

        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, "knowledgeArticlesServiceUser");
        ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param);
        Session session = resolver.adaptTo(Session.class);

        try {
            if (session != null) {

                /* Change the article page name and path as per the actual site's structure */
                Resource resource = resolver.getResource("/content/solidigm/language-masters/en/support");
                if (resource != null) {
                    Node jcrContent = resource.adaptTo(Node.class);

                    if (knowledgeArticle != null) {

                        Node articleNode = null;
                        if (!jcrContent.hasNode(knowledgeArticle.getArticleId())) {
                            String pagePath = jcrContent.getPath();
                            String pageName = knowledgeArticle.getArticleId();
                            String pageTitle = knowledgeArticle.getTitle();
                            String template = "/conf/solidigm/settings/wcm/templates/flexible-content-page";
                            String renderer = "solidigm/components/page/flexibleContent";

                            createKnowledgeArticlePage(resolver, pagePath, pageName, pageTitle, template, renderer);
                        }

                        String articleNodeName = knowledgeArticle.getArticleId();
                        articleNode = jcrContent.getNode(articleNodeName);
                        if (articleNode != null && articleNode.isNode()) {

                            String nodePath = articleNodeName + "/jcr:content";
                            Node articleJcrNode = jcrContent.getNode(nodePath);
                            if (articleJcrNode != null && articleJcrNode.isNode()) {

                                /* Add/update the article page properties as per the design */
                                articleJcrNode.setProperty("articleId", knowledgeArticle.getArticleId());
                                articleJcrNode.setProperty("title", knowledgeArticle.getTitle());
                                articleJcrNode.setProperty("jcr:description", knowledgeArticle.getDescription());
                                articleJcrNode.setProperty("content", knowledgeArticle.getContent());
                                articleJcrNode.setProperty("articleCreatedOn", knowledgeArticle.getCreatedOn());
                                articleJcrNode.setProperty("articleModifiedOn", knowledgeArticle.getModifiedOn());

                                session.save();
                                session.refresh(true);

                            }

                            /* Used standard parsys with content-page type */
                            Node responsiveNode = articleJcrNode.getNode("root/container/container");
                            Node textNode = null;

                            /* Set Article Title */
                            if (!responsiveNode.hasNode("title")) {
                                textNode = responsiveNode.addNode("title");
                            }

                            textNode = responsiveNode.getNode("title");
                            textNode.setProperty("sling:resourceType", "solidigm/components/knowledgearticletext");

                            textNode.setProperty("text", "<h2>" + knowledgeArticle.getTitle().toString() + "<h2>");

                            /*Set Article Description*/
                            if (!responsiveNode.hasNode("articleDescription")) {
                                textNode = responsiveNode.addNode("articleDescription");
                            }

                            textNode = responsiveNode.getNode("articleDescription");

                            textNode.setProperty("sling:resourceType", "solidigm/components/knowledgearticletext");
                            textNode.setProperty("text", "<h3>" + knowledgeArticle.getDescription().toString() + "<h3>");

                            /*Set Article Content*/
                            if (!responsiveNode.hasNode("articleContent")) {
                                textNode = responsiveNode.addNode("articleContent");
                            }

                            textNode = responsiveNode.getNode("articleContent");

                            textNode.setProperty("sling:resourceType", "solidigm/components/knowledgearticletext");
                            textNode.setProperty("text", knowledgeArticle.getContent().toString());

                            session.save();
                            session.refresh(true);

                        }
                    }
                }

            }
        } catch (RepositoryException repositoryException) {
            logger.error("Repository Exception {}", repositoryException);
        } catch (Exception e) {
            logger.error("LoginException {}", e);
        }
        session.logout();
        return null;
    }

    public List<String> buildArticleNodeList(Page rootPage) {
        List<String> articleNodeList = new ArrayList<>();

        try {
            if (rootPage != null) {
                for (Iterator<Page> it = rootPage.listChildren(); it.hasNext(); ) {
                    Page articlePage = it.next();
                    articleNodeList.add(articlePage.getName());
                }
            }
        } catch (Exception e) {
            logger.error("LoginException {}", e);
        }
        return articleNodeList;
    }

    public String removeKnowledgeArticleNode(Session session, Node knowledgeArticleNode) {
        try {
            if (knowledgeArticleNode != null) {
                knowledgeArticleNode.remove();
                session.save();
                session.refresh(true);
            }

        } catch (Exception e) {
            logger.error("LoginException {}", e);
        }
        return "Success";
    }

    public String createKnowledgeArticlePage(ResourceResolver resourceResolver, String path, String pageName, String pageTitle, String template, String renderer) throws LoginException {

        Page articlePage = null;
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, "knowledgeArticlesServiceUser");
        ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param);
        Session session = resolver.adaptTo(Session.class);

        try {
            if (session != null) {

                // Create Page
                PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
                articlePage = pageManager.create(path, pageName, template, pageTitle);
                Node pageNode = articlePage.adaptTo(Node.class);

                Node jcrNode = null;
                if (!articlePage.hasContent()) {
                    pageNode.addNode("jcr:content", "cq:PageContent");
                }
                jcrNode = articlePage.getContentResource().adaptTo(Node.class);
                jcrNode.setProperty("sling:resourceType", renderer);

                session.save();
                session.refresh(true);

            }

        } catch (Exception e) {
            logger.error("LoginException {}", e);
        }
        session.logout();
        return "Success";
    }

    public void processArticles(String articleJson) throws UnsupportedEncodingException, LoginException {
        List<KnowledgeArticle> KnowledgeArticleList = parseKnowledgeArticleResponse(articleJson);
        processKnowledgeArticleList(KnowledgeArticleList);
    }

}
