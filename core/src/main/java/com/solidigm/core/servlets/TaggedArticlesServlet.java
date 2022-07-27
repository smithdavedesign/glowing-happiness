package com.solidigm.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.TagManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.solidigm.core.beans.ArticlePage;
import com.solidigm.core.utils.ImageUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet that takes the current page, articles per page, and selected tag of the taggedArticles component.
 * It then returns any article pages matching that tag, offset by the current page * articles per page, only returns
 * an amount equal to articles per page of articles at a time.
 */
@Component(service = { Servlet.class })
@SlingServletPaths("/bin/solidigm/taggedArticles")
@ServiceDescription("Simple Demo Servlet")
public class TaggedArticlesServlet extends SlingSafeMethodsServlet {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_ARTICLES_PER_PAGE = 10;
    private static final String ARTICLE_TEMPLATE = "/conf/solidigm/settings/wcm/templates/article-page";

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        ResourceResolver resourceResolver = req.getResourceResolver();

        //Get request params
        RequestParameter pageParam = req.getRequestParameter("page");
        RequestParameter articlesPerPageParam = req.getRequestParameter("articlesPerPage");
        RequestParameter tagParam = req.getRequestParameter("tag");

        //Verify and process page param
        int page = DEFAULT_PAGE;
        if (pageParam != null) {
            try {
                if(Integer.parseInt(pageParam.getString()) > 1) {
                    page = Integer.parseInt(pageParam.getString());
                }
            } catch (NumberFormatException e) {
                log.error("TaggedArticlesServlet: unable to convert requested page to integer", e);
            }
        }

        //Verify and process articlesPerPage param
        int articlesPerPage = DEFAULT_ARTICLES_PER_PAGE;
        if (articlesPerPageParam != null) {
            try {
                if(Integer.parseInt(articlesPerPageParam.getString()) > 0) {
                    articlesPerPage = Integer.parseInt(articlesPerPageParam.getString());
                }
            } catch (NumberFormatException e) {
                log.error("TaggedArticlesServlet: unable to convert requested articlesPerPage to integer", e);
            }
        }

        //Verify and set tag param
        String tag = tagParam == null ? "" : tagParam.getString();

        //Build the articles query
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", "/content/solidigm");
        queryMap.put("type", "cq:Page");
        queryMap.put("property", "jcr:content/cq:template");
        queryMap.put("property.operation", "equals");
        queryMap.put("property.value", ARTICLE_TEMPLATE);
        queryMap.put("orderby", "jcr:content/articleDate");
        queryMap.put("orderby.sort", "desc");
        queryMap.put("p.limit", String.valueOf(articlesPerPage));
        queryMap.put("p.offset", String.valueOf(articlesPerPage * (page - 1)));
        if(StringUtils.isNotEmpty(tag)) {
            queryMap.put("group.p.or", "true");
            queryMap.put("group.1_group.property", "jcr:content/articleCategory");
            queryMap.put("group.1_group.property.operation", "equals");
            queryMap.put("group.1_group.property.value", tag);
            queryMap.put("group.2_group.property", "jcr:content/cq:tags");
            queryMap.put("group.2_group.property.operation", "equals");
            queryMap.put("group.2_group.property.value", tag);
        }

        //Execute the query and evaluate the results
        Session session = resourceResolver.adaptTo(Session.class);
        SearchResult result = queryBuilder.createQuery(PredicateGroup.create(queryMap), session).getResult();
        long totalMatches = result.getTotalMatches();
        int totalPages = (int) Math.ceil((double) totalMatches / articlesPerPage);

        //Iterate through the hits, processing each article page and adding them to the JSON Array
        List<Hit> hits = result.getHits();
        JsonArray articlesArray = new JsonArray();
        for (Hit hit : hits) {
            try {
                String articlePath = hit.getPath();
                if(StringUtils.isNotEmpty(articlePath)) {
                    Resource articlePageResource = resourceResolver.getResource(articlePath.concat("/jcr:content"));
                    if (articlePageResource != null) {
                        //Get article page properties and add to json array
                        ArticlePage articlePage = articlePageResource.adaptTo(ArticlePage.class);
                        JsonObject articleProps = new JsonObject();
                        articleProps.addProperty("teaserImage", articlePage.getTeaserImage());
                        articleProps.addProperty("articleTitle", articlePage.getArticleTitle());
                        articleProps.addProperty("articleLink", articlePath.concat(".html"));

                        //Resolve category tag title
                        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
                        articleProps.addProperty("categoryTag",
                                                 tagManager.resolve(articlePage.getArticleCategory()).getTitle());

                        //Get alt text from DAM if it is not set for page
                        if(StringUtils.isEmpty(articlePage.getTeaserImageAltText())
                            && StringUtils.isNotEmpty(articlePage.getTeaserImage())) {
                            articleProps.addProperty("altText", ImageUtil.getAltTextFromDAM(resourceResolver,
                                                                                            articlePage.getTeaserImage()));
                        } else {
                            articleProps.addProperty("altText", articlePage.getTeaserImageAltText());
                        }

                        articlesArray.add(articleProps);
                    }
                }
            } catch (RepositoryException e) {
                log.error("Error adding article to response", e);
            }
        }

        //Build response JSON
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("page", page);
        responseObject.addProperty("articlesPerPage", articlesPerPage);
        responseObject.addProperty("tag", tag);
        responseObject.addProperty("totalMatches", totalMatches);
        responseObject.addProperty("totalPages", totalPages);
        responseObject.add("articles", articlesArray);

        resp.setContentType("application/json");
        resp.getWriter().write(responseObject.toString());
    }
}
