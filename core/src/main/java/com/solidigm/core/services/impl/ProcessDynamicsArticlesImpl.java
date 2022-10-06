package com.solidigm.core.services.impl;

import com.solidigm.core.beans.KnowledgeArticle;
import com.solidigm.core.services.ProcessDynamicsArticles;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.microsoft.aad.msal4j.*;
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
        Set<String> SCOPE = Collections.singleton("https://nandqa.api.crm.dynamics.com/api/data/v9.2/knowledgearticles/.default");
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
            HttpGet request = new HttpGet("https://nandqa.api.crm.dynamics.com/api/data/v9.2/knowledgearticles");

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
        return "no worky";
    }

    public List<KnowledgeArticle> parseKnowledgeArticleResponse(String articleJson)
            throws UnsupportedEncodingException {

        JSONObject result = new JSONObject();
        List<KnowledgeArticle> knowledgeArticleList = null;

        try {

            if(articleJson!=null) {
                String jsonString = articleJson;
                jsonString = "{\n  \"@odata.context\": \"https://org9bb5e15d.api.crm8.dynamics.com/api/data/v9.2/$metadata#knowledgearticles\",\n  \"value\": [\n    {\n      \"@odata.etag\": \"W/\\\"3035244\\\"\",\n      \"isrootarticle\": false,\n      \"msdyn_isingestedarticle\": false,\n      \"statecode\": 3,\n      \"islatestversion\": true,\n      \"knowledgearticleviews_state\": 1,\n      \"statuscode\": 7,\n      \"description\": \"Troubleshooting and Service Fixes: Coffee Grounds\",\n      \"setproductassociations\": false,\n      \"knowledgearticleviews\": 0,\n      \"importsequencenumber\": 105,\n      \"rating_state\": 1,\n      \"minorversionnumber\": 0,\n      \"readyforreview\": false,\n      \"_rootarticleid_value\": \"4d30deaa-b4df-ec11-bb3d-6045bdac6c5f\",\n      \"_ownerid_value\": \"282587a0-e18d-eb11-b1ac-000d3ae92b46\",\n      \"modifiedon\": \"2022-05-30T01:07:02Z\",\n      \"msdyn_keywordsdescsuggestioncontrol\": false,\n      \"rating_date\": \"2022-06-07T12:43:13Z\",\n      \"title\": \"Why Are There Coffee Grounds in My Cup After Brewing?\",\n      \"rating_count\": 0,\n      \"majorversionnumber\": 1,\n      \"versionnumber\": 3035244,\n      \"rating_sum\": 0.0000000000,\n      \"createdon\": \"2022-05-30T01:06:04Z\",\n      \"isprimary\": true,\n      \"content\": \"<p><u><span style=\\\"font-size:18px;\\\"><span style=\\\"font-family:'Segoe UI','Helvetica Neue',sans-serif;\\\">You love coffee, but who wants to drink the grounds? Not you.</span></span></u></p>\\n\\n<p style=\\\"font-family:Segoe UI,sans-serif;font-size:12.5pt;background:white;color:black;\\\">Check your filter baskets. If there are any dents, Please change same. Your&#160;machine works off of pressure, and a dented filter basket will not be able to hold the pressure needed to brew the espresso properly.</p>\\n\\n<p style=\\\"font-family:Segoe UI,sans-serif;font-size:12.5pt;background:white;color:black;\\\">Also, check the group seals. These do not last forever. They have to be replaced sometimes, usually about yearly. Yours may be worn out. The group seals need to be cleaned off with a brush between uses too.</p>\\n\\n<p style=\\\"font-family:Segoe UI,sans-serif;font-size:12.5pt;background:white;color:black;\\\">If there are grounds on the rim of the filter basket, your machine will not seal properly for brewing, causing grounds to end up in your cup of espresso.</p>\\n\\n<p>.</p>\\n\\n<p>&#160;</p>\\n\\n<p>&#160;</p>\\n\\n<p>&#160;</p>\\n\\n<p>&#160;</p>\\n\\n\\n\",\n      \"knowledgearticleviews_date\": \"2022-06-07T12:43:13Z\",\n      \"updatecontent\": false,\n      \"_languagelocaleid_value\": \"56940b3e-300f-4070-a559-5a6a4d11a8a3\",\n      \"articlepublicnumber\": \"KA-01002\",\n      \"_modifiedby_value\": \"b88225d8-30df-ec11-bb3d-6045bdac6c5f\",\n      \"msdyn_iscontentsyncedtostore\": false,\n      \"knowledgearticleid\": \"31993caf-b90e-ea11-a813-000d3a1b1223\",\n      \"_createdby_value\": \"b88225d8-30df-ec11-bb3d-6045bdac6c5f\",\n      \"_owningbusinessunit_value\": \"c67b25d8-30df-ec11-bb3d-6045bdac6c5f\",\n      \"_parentarticlecontentid_value\": \"4d30deaa-b4df-ec11-bb3d-6045bdac6c5f\",\n      \"isinternal\": false,\n      \"rating\": 0.0000000000,\n      \"_owninguser_value\": \"282587a0-e18d-eb11-b1ac-000d3ae92b46\",\n      \"keywords\": \"Coffee Grounds\",\n      \"setcategoryassociations\": false,\n      \"_primaryauthorid_value\": null,\n      \"expirationdate\": null,\n      \"scheduledstatusid\": null,\n      \"stageid\": null,\n      \"_modifiedonbehalfby_value\": null,\n      \"processid\": null,\n      \"msdyn_contentstore_name\": null,\n      \"expiredreviewoptions\": null,\n      \"utcconversiontimezonecode\": null,\n      \"traversedpath\": null,\n      \"_createdonbehalfby_value\": null,\n      \"expirationstatusid\": null,\n      \"msdyn_retrycountformigrationtocontentstore\": null,\n      \"_owningteam_value\": null,\n      \"expirationstateid\": null,\n      \"msdyn_ingestedarticleurl\": null,\n      \"publishstatusid\": null,\n      \"_previousarticlecontentid_value\": null,\n      \"exchangerate\": null,\n      \"publishon\": null,\n      \"msdyn_contentstore\": null,\n      \"timezoneruleversionnumber\": null,\n      \"overriddencreatedon\": null,\n      \"_transactioncurrencyid_value\": null,\n      \"_subjectid_value\": null,\n      \"review\": null\n    },\n    {\n      \"@odata.etag\": \"W/\\\"3035252\\\"\",\n      \"isrootarticle\": false,\n      \"msdyn_isingestedarticle\": false,\n      \"statecode\": 3,\n      \"islatestversion\": true,\n      \"knowledgearticleviews_state\": 1,\n      \"statuscode\": 7,\n      \"description\": \"Returns and Warranty: Return and Exchange\",\n      \"setproductassociations\": true,\n      \"knowledgearticleviews\": 0,\n      \"importsequencenumber\": 105,\n      \"rating_state\": 1,\n      \"minorversionnumber\": 0,\n      \"readyforreview\": false,\n      \"_rootarticleid_value\": \"5730deaa-b4df-ec11-bb3d-6045bdac6c5f\",\n      \"_ownerid_value\": \"282587a0-e18d-eb11-b1ac-000d3ae92b46\",\n      \"modifiedon\": \"2022-05-30T01:07:03Z\",\n      \"msdyn_keywordsdescsuggestioncontrol\": false,\n      \"rating_date\": \"2022-06-07T12:43:13Z\",\n      \"title\": \"Return and Exchange\",\n      \"rating_count\": 0,\n      \"majorversionnumber\": 1,\n      \"versionnumber\": 3035252,\n      \"rating_sum\": 0.0000000000,\n      \"createdon\": \"2022-05-30T01:06:05Z\",\n      \"isprimary\": true,\n      \"content\": \"<p><span style=\\\"font-family:Segoe UI,sans-serif;font-size:12.5pt;background:white;color:black;\\\">\\n<strong>Returns and Exchanges</strong>\\n<br>Pressure - For purchases made directly from the any of our physical stores, we offer a 30-day return for exchange/credit from date of purchase, if for any reason you are dissatisfied with a purchase other than the Exceptions below.\\n\\n<br><br><strong>Exceptions</strong>\\n<br>Unless the product arrives damaged or defective, our 30-day return policy excludes the following since we cannot restock these items for resale:\\n<br>Closeout and refurbished products\\n<br>Software downloads\\n\\n<br><br><strong>Return Instructions</strong>\\n<br>All merchandise for return must be in new condition, with its original packaging. Any accompanying manuals, and/or or supplies must be returned with the product. If product returned to us is incomplete or in less than original condition, we reserve the right to assess a 10% service charge and bill it directly to your credit card.\\n<br><br>To return a package, follow the return instructions given by the return representative. Return the package within 7 days to our Indiana facility, via prepaid, insured, traceable method to ensure a safe and documented delivery to us. Contoso&#160;cannot be responsible for goods lost or damaged in transit to our warehouse. Note: For damaged, defective or incorrect product, Contoso will cover the cost of the return shipping through issuance of a FedEx call tag.\\n</span></p>\",\n      \"knowledgearticleviews_date\": \"2022-06-07T12:43:13Z\",\n      \"updatecontent\": false,\n      \"_languagelocaleid_value\": \"56940b3e-300f-4070-a559-5a6a4d11a8a3\",\n      \"articlepublicnumber\": \"KA-01005\",\n      \"_modifiedby_value\": \"b88225d8-30df-ec11-bb3d-6045bdac6c5f\",\n      \"msdyn_iscontentsyncedtostore\": false,\n      \"knowledgearticleid\": \"37993caf-b90e-ea11-a813-000d3a1b1223\",\n      \"_createdby_value\": \"b88225d8-30df-ec11-bb3d-6045bdac6c5f\",\n      \"_owningbusinessunit_value\": \"c67b25d8-30df-ec11-bb3d-6045bdac6c5f\",\n      \"_parentarticlecontentid_value\": \"5730deaa-b4df-ec11-bb3d-6045bdac6c5f\",\n      \"isinternal\": false,\n      \"rating\": 0.0000000000,\n      \"_owninguser_value\": \"282587a0-e18d-eb11-b1ac-000d3ae92b46\",\n      \"keywords\": \"Pressure\",\n      \"setcategoryassociations\": false,\n      \"_primaryauthorid_value\": null,\n      \"expirationdate\": null,\n      \"scheduledstatusid\": null,\n      \"stageid\": null,\n      \"_modifiedonbehalfby_value\": null,\n      \"processid\": null,\n      \"msdyn_contentstore_name\": null,\n      \"expiredreviewoptions\": null,\n      \"utcconversiontimezonecode\": null,\n      \"traversedpath\": null,\n      \"_createdonbehalfby_value\": null,\n      \"expirationstatusid\": 10,\n      \"msdyn_retrycountformigrationtocontentstore\": null,\n      \"_owningteam_value\": null,\n      \"expirationstateid\": 4,\n      \"msdyn_ingestedarticleurl\": null,\n      \"publishstatusid\": null,\n      \"_previousarticlecontentid_value\": null,\n      \"exchangerate\": null,\n      \"publishon\": null,\n      \"msdyn_contentstore\": null,\n      \"timezoneruleversionnumber\": 0,\n      \"overriddencreatedon\": null,\n      \"_transactioncurrencyid_value\": null,\n      \"_subjectid_value\": null,\n      \"review\": 0\n    }]\n}";

                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray knowledgeArticles = (JSONArray) jsonObject.get("value");

                knowledgeArticleList = new ArrayList<>();

                for (int i = 0; i < knowledgeArticles.length(); i++) {
                    KnowledgeArticle knowledgeArticle = new KnowledgeArticle();
                    knowledgeArticle.setArticleId(knowledgeArticles.getJSONObject(i).getString("knowledgearticleid"));
                    knowledgeArticle.setTitle(knowledgeArticles.getJSONObject(i).getString("title"));
                    knowledgeArticle.setDescription(knowledgeArticles.getJSONObject(i).getString("description"));
                    knowledgeArticle.setContent(knowledgeArticles.getJSONObject(i).getString("content"));
                    knowledgeArticle.setCreatedOn(knowledgeArticles.getJSONObject(i).getString("createdon"));
                    knowledgeArticle.setModifiedOn(knowledgeArticles.getJSONObject(i).getString("modifiedon"));

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

            /*Add or modify article pages */
            for (KnowledgeArticle knowledgeArticle : knowledgeArticleList) {
                addKnowledgeArticleNode(resolverFactory, knowledgeArticle);
            }

            String path = "/content/solidigm/language-masters/en/support";
            Resource resource = resolver.getResource(path);
            Node jcrContent = resource.adaptTo(Node.class);

            /*Delete article pages */
            List<String> removeArticleNodeList = buildArticleNodeList(resource);
            if (!removeArticleNodeList.isEmpty() && !knowledgeArticleList.isEmpty()) {
                for (int i = 0; i < removeArticleNodeList.size(); i++) {
                    for (KnowledgeArticle knowledgeArticle : knowledgeArticleList) {
                        if (removeArticleNodeList.get(i).equals(knowledgeArticle.getArticleId())) {
                            removeArticleNodeList.remove(i);
                        }
                    }
                }
            }

            if (!removeArticleNodeList.isEmpty()) {
                for (String articleNode : removeArticleNodeList) {

                    Node removeArticleNode = jcrContent.getNode(articleNode);
                    if (removeArticleNode != null) {
                        removeKnowledgeArticleNode(session, removeArticleNode);
                    }
                }
            }

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
                                articleJcrNode.setProperty("articleId", knowledgeArticle.getArticleId().toString());
                                articleJcrNode.setProperty("title", knowledgeArticle.getTitle().toString());
                                articleJcrNode.setProperty("jcr:description", knowledgeArticle.getDescription().toString());
                                articleJcrNode.setProperty("content", knowledgeArticle.getContent().toString());
                                articleJcrNode.setProperty("articleCreatedOn", knowledgeArticle.getCreatedOn().toString());
                                articleJcrNode.setProperty("articleModifiedOn", knowledgeArticle.getModifiedOn().toString());

                                session.save();
                                session.refresh(true);

                            }

                            /* Used standard parsys with content-page type */
                            Node responsiveNode = articleJcrNode.getNode("root/container/container");
                            Node titleNode = null;
                            Node textNode = null;

                            /* Set Article Title */
                            if (!responsiveNode.hasNode("title")) {
                                titleNode = responsiveNode.addNode("title");
                            }

                            titleNode = responsiveNode.getNode("title");
                            titleNode.setProperty("sling:resourceType", "solidigm/components/title");

                            titleNode.setProperty("jcr:title", knowledgeArticle.getTitle().toString());
                            titleNode.setProperty("type", "h1");

                            /*Set Article Details*/
                            if (!responsiveNode.hasNode("articleDetails")) {
                                textNode = responsiveNode.addNode("articleDetails");
                            }

                            textNode = responsiveNode.getNode("articleDetails");

                            String articleDetails = "<h2>Article ID: " + knowledgeArticle.getArticleId().toString() + "</h2> <b> </b> <h5> Modified On: " + knowledgeArticle.getModifiedOn() + "</h5>";

                            textNode.setProperty("sling:resourceType", "solidigm/components/text");
                            textNode.setProperty("text", articleDetails);
                            textNode.setProperty("textIsRich", "true");

                            /*Set Article Description*/
                            if (!responsiveNode.hasNode("articleDescription")) {
                                textNode = responsiveNode.addNode("articleDescription");
                            }

                            textNode = responsiveNode.getNode("articleDescription");

                            textNode.setProperty("sling:resourceType", "solidigm/components/text");
                            textNode.setProperty("text", "<h2>" + knowledgeArticle.getDescription().toString() + "<h2>");
                            textNode.setProperty("textIsRich", "true");

                            /*Set Article Content*/
                            if (!responsiveNode.hasNode("articleContent")) {
                                textNode = responsiveNode.addNode("articleContent");
                            }

                            textNode = responsiveNode.getNode("articleContent");

                            textNode.setProperty("sling:resourceType", "solidigm/components/text");
                            textNode.setProperty("text", knowledgeArticle.getContent().toString());
                            textNode.setProperty("textIsRich", "true");

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

    public List<String> buildArticleNodeList(Resource resource) {
        List<String> articleNodeList = new ArrayList<>();

        try {
            if (resource != null) {

                Iterator<Resource> iterator = resource.getChildren().iterator();
                while (iterator.hasNext()) {
                    Resource articleResource = iterator.next();
                    articleNodeList.add(articleResource.getName());
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
                if (articlePage.hasContent()) {
                    jcrNode = articlePage.getContentResource().adaptTo(Node.class);
                } else {
                    jcrNode = pageNode.addNode("jcr:content", "cq:PageContent");
                }
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
