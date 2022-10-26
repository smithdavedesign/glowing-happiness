package com.solidigm.core.services.impl;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.aad.msal4j.*;
import com.solidigm.core.beans.KnowledgeArticle;
import com.solidigm.core.services.DynamicsArticlesService;
import com.solidigm.core.services.DynamicsArticlesServiceConfig;
import com.solidigm.core.utils.SolidigmConstants;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;

@Component(service = DynamicsArticlesService.class, immediate = true)
@Designate(ocd = DynamicsArticlesServiceConfig.class)
public class DynamicsArticlesServiceImpl implements DynamicsArticlesService {

    /* The Constant logger */
    private static final Logger logger = LoggerFactory.getLogger(DynamicsArticlesServiceImpl.class);

    private String supportPagePath;

    private Map<String, String> articleCategories = new HashMap<>();

    private String authority;

    private String clientId;

    private String clientSecret;

    private String apiUrl;

    @Activate
    public void activate(DynamicsArticlesServiceConfig config) {
        supportPagePath = config.getSupportPagePath();
        authority = config.getAuthority();
        clientId = config.getClientId();
        clientSecret = config.getClientSecret();
        apiUrl = config.getApiUrl();
        for (String indexPage : config.getSupportIndexPages()) {
            String indexPageName = indexPage.split(",")[0];
            String typeCode = indexPage.split(",")[1];
            articleCategories.put(typeCode, indexPageName);
        }
        if (config.getRunOnSave()) {
            logger.info("Running Knowledge Articles sync from dynamics");
            String accessToken = acquireToken();
            String articleJson = fetchArticles(accessToken);
            processArticles(articleJson);
        }
    }

    @Reference
    private transient ResourceResolverFactory resolverFactory;

    /**
     * Parse the JsonObject and return the map of Articles within with keys matching the ID
     * @param articleJson
     * @return Map<String, KnowledgeArticle>
     */
    private Map<String, KnowledgeArticle> parseKnowledgeArticleResponse(JsonObject articleJson) {
        Map<String, KnowledgeArticle> knowledgeArticleMap = new HashMap<>();
        JsonArray knowledgeArticles = articleJson.getAsJsonArray("value");

        for (int i = 0; i < knowledgeArticles.size(); i++) {
            JsonObject article = knowledgeArticles.get(i).getAsJsonObject();
            KnowledgeArticle knowledgeArticle = new KnowledgeArticle();
            knowledgeArticle.setArticleId(article.get("knowledgearticleid").getAsString());
            knowledgeArticle.setTitle(article.get("title").getAsString());
            knowledgeArticle.setDescription(article.get("description").getAsString());
            knowledgeArticle.setContent(StringEscapeUtils.unescapeJava(
                    article.get("content").getAsString().replaceAll("\"", "'")));
            knowledgeArticle.setCreatedOn(article.get("createdon").getAsString());
            knowledgeArticle.setModifiedOn(article.get("modifiedon").getAsString());
            knowledgeArticle.setCategoryCode(article.get("nand_articlecontenttypecode").getAsString().split(",")[0]);
            knowledgeArticle.setArticlePublicId(article.get("articlepublicnumber").getAsString());
            knowledgeArticleMap.put(knowledgeArticle.getArticleId(), knowledgeArticle);
        }

        return knowledgeArticleMap;
    }

    /**
     * Creates new KA page or updates an existing KA page with the content in the knowledge article param
     * @param knowledgeArticle - Object containing content for KA page
     * @param resolver - a signed-in resolver with write properties in /content/solidigm
     * @param session - The session being used for this KA processing step
     */
    private void addKnowledgeArticleNode(KnowledgeArticle knowledgeArticle, ResourceResolver resolver, Session session) {
        try {
            if (session != null) {

                /* Change the article page name and path as per the actual site's structure */
                Resource resource = resolver.getResource(supportPagePath);

                if (resource != null) {
                    Node jcrContent = resource.adaptTo(Node.class);

                    if (knowledgeArticle != null) {
                        String articleCategory = articleCategories.get(knowledgeArticle.getCategoryCode());
                        if (articleCategory != null && jcrContent != null && jcrContent.hasNode(articleCategory)) {
                            jcrContent = jcrContent.getNode(articleCategory);
                            String knowledgeArticleName = knowledgeArticle.getArticlePublicId().toLowerCase();
                            Node articleNode = null;
                            if (!jcrContent.hasNode(knowledgeArticleName)) {
                                String pagePath = jcrContent.getPath();
                                String pageTitle = knowledgeArticle.getTitle();
                                String template = SolidigmConstants.FLEXIBLE_CONTENT_PAGE_TEMPLATE;
                                String renderer = "solidigm/components/page/flexibleContent";

                                createKnowledgeArticlePage(resolver, session, pagePath, knowledgeArticleName, pageTitle, template, renderer);
                            }

                            articleNode = jcrContent.getNode(knowledgeArticleName);
                            if (articleNode != null && articleNode.isNode()) {

                                String nodePath = knowledgeArticleName + "/jcr:content";
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

                                    /* Used standard parsys with content-page type */
                                    Node responsiveNode = articleJcrNode.getNode("root/container");
                                    Node textNode = null;

                                    /* Set Article Title */
                                    if (!responsiveNode.hasNode("title")) {
                                        textNode = responsiveNode.addNode("title");
                                    }

                                    textNode = responsiveNode.getNode("title");
                                    textNode.setProperty("sling:resourceType",
                                                         "solidigm/components/knowledgearticletext");

                                    textNode.setProperty("text",
                                                         "<h2>" + knowledgeArticle.getTitle().toString() + "<h2>");

                                    /*Set Article Description*/
                                    if (!responsiveNode.hasNode("articleDescription")) {
                                        textNode = responsiveNode.addNode("articleDescription");
                                    }

                                    textNode = responsiveNode.getNode("articleDescription");

                                    textNode.setProperty("sling:resourceType",
                                                         "solidigm/components/knowledgearticletext");
                                    textNode.setProperty("text",
                                                         "<h3>" + knowledgeArticle.getDescription()
                                                                 .toString() + "<h3>");

                                    /*Set Article Content*/
                                    if (!responsiveNode.hasNode("articleContent")) {
                                        textNode = responsiveNode.addNode("articleContent");
                                    }

                                    textNode = responsiveNode.getNode("articleContent");

                                    textNode.setProperty("sling:resourceType",
                                                         "solidigm/components/knowledgearticletext");
                                    textNode.setProperty("text", knowledgeArticle.getContent().toString());

                                    session.save();
                                    session.refresh(true);

                                }
                            }
                        }
                    }
                }

            }
        } catch (RepositoryException repositoryException) {
            logger.error("Unable to save changes to JCR", repositoryException);
        }
    }

    /**
     * Removes the specified KA node from the JCR
     * @param session - the signed-in Session being used for this KA processing step
     * @param knowledgeArticleNode - Node to be deleted
     */
    private void removeKnowledgeArticleNode(Session session, Node knowledgeArticleNode) {
        try {
            if (knowledgeArticleNode != null) {
                knowledgeArticleNode.remove();
                session.save();
                session.refresh(true);
            }

        } catch (RepositoryException e) {
            logger.error("Unable to save JCR changes: Deleting KA node", e);
        }
    }

    /**
     * Creates a KA page with the properties specified in the params
     * @param resolver - a signed-in Resource Resolver with permission to write to /content/solidigm
     * @param session - the signed-in Session being used for this KA processing step
     * @param path - The path to create the new page
     * @param pageName - the name of the new page
     * @param pageTitle - The title of the new page
     * @param template - the template to use for the new page
     * @param renderer - The page renderer component to use for the new page
     */
    public void createKnowledgeArticlePage(ResourceResolver resolver, Session session, String path, String pageName, String pageTitle, String template, String renderer) {
        Page articlePage = null;

        try {
            if (session != null) {

                // Create Page
                logger.info("Creating new page: " + pageName + "; at: " + path);
                PageManager pageManager = resolver.adaptTo(PageManager.class);
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

        } catch (RepositoryException e) {
            logger.error("Error saving changes to the JCR", e);
        } catch (WCMException e) {
            logger.error("Error creating page", e);
        }
    }

    public String acquireToken() {

        Set<String> SCOPE = Collections.singleton(apiUrl.substring(0, apiUrl.indexOf(".com/") + 5).concat(".default"));
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
                            .build();

            result = cca.acquireToken(parameters).join();
            accessToken = result.accessToken();

        } catch (Exception e) {
            logger.error("Error acquiring token from microsoft", e);
        }
        return accessToken;
    }

    public String fetchArticles(String accessToken){

        String articleJson = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(apiUrl);

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
            logger.error("Error fetching articles", e);
        }
        return articleJson;
    }

    public void processArticles(String articleJson) {
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, "knowledgeArticlesServiceUser");
        ResourceResolver resolver = null;
        try {
            resolver = resolverFactory.getServiceResourceResolver(param);
        } catch (LoginException e) {
            logger.error("Unable to sign in Knowledge Articles Service User");
            return;
        }
        Session session = resolver.adaptTo(Session.class);

        if (articleJson != null) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(articleJson, JsonObject.class);
            Map<String, KnowledgeArticle> knowledgeArticles = parseKnowledgeArticleResponse(jsonObject);

            Page rootPage = resolver.getResource("/content/solidigm/language-masters/en/support-page").adaptTo(Page.class);
            if (rootPage != null) {
                //Update KA's existing in AEM
                for (Iterator<Page> categoryIterator = rootPage.listChildren(); categoryIterator.hasNext(); ) {
                    Page categoryPage = categoryIterator.next();
                    for (Iterator<Page> articleIterator = categoryPage.listChildren(); articleIterator.hasNext(); ) {
                        Page articlePage = articleIterator.next();
                        ValueMap articleProperties = articlePage.getContentResource().getValueMap();
                        String articlePageID = articleProperties.get("articleId", String.class);
                        if (StringUtils.isNotEmpty(articlePageID)) {
                            if (knowledgeArticles.containsKey(articlePageID)) {
                                KnowledgeArticle knowledgeArticle = knowledgeArticles.get(articlePageID);
                                String pageLastModified = articleProperties.get("articleModifiedOn", String.class);

                                //If this page has been corrupted or is out of date, update it
                                if (StringUtils.isEmpty(pageLastModified) ||
                                        !pageLastModified.equals(knowledgeArticle.getModifiedOn())) {
                                    removeKnowledgeArticleNode(session, articlePage.adaptTo(Node.class));
                                    addKnowledgeArticleNode(knowledgeArticle, resolver, session);
                                }

                                //Remove KA from map so that it is not added as a new page later
                                knowledgeArticles.remove(articlePageID);
                            } else {
                                //If this page is not found in the API response, delete it in AEM
                                removeKnowledgeArticleNode(session, articlePage.adaptTo(Node.class));
                            }
                        } else {
                            //If this page was missing an article ID delete it
                            removeKnowledgeArticleNode(session, articlePage.adaptTo(Node.class));
                        }
                    }
                }
            }

            //Add any pages that don't exist yet in AEM
            for (Map.Entry<String, KnowledgeArticle> entry : knowledgeArticles.entrySet()) {
                KnowledgeArticle knowledgeArticle = entry.getValue();
                addKnowledgeArticleNode(knowledgeArticle, resolver, session);
            }
        }

        session.logout();
    }

}
