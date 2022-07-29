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

import static com.solidigm.core.utils.SolidigmConstants.ARTICLE_TEMPLATE;
import static com.solidigm.core.utils.SolidigmConstants.EVENT_TEMPLATE;

/**
 * Servlet that takes the root path, current page, results per page, and selected tag of the taggedArticles component.
 * It then returns any article pages matching that tag, offset by the current page * articles per page, only returns
 * an amount equal to articles per page of articles at a time.
 *
 * Parameters:
 * rootPath (Required) {String}: the localized root path of the site to search on
 * currentPage (Default: 0) {int}: the index of the current page of results
 * resultsPerPage (Default: 10) {int}: the number of results to display on a single page of results
 * tag (Optional) {String}: the tag to filter the results by
 *
 * Returns:
 * rootPath {String}: copy of what was sent
 * currentPage {int}: copy of what was sent
 * resultsPerPage {int}: copy of what was sent
 * tag {String}: copy of what was sent
 * totalMatches {int}: the total number of matches the query found
 * totalPages {int}: the total number of pages that the query would display given the provided results per page
 * results {Array}: list of result objects with properties for image, title, link, tag, and alt text
 */
@Component(service = { Servlet.class })
@SlingServletPaths("/bin/solidigm/taggedArticles")
@ServiceDescription("Tagged Articles Servlet Servlet")
public class TaggedArticlesServlet extends SlingSafeMethodsServlet {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_ARTICLES_PER_PAGE = 10;

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        ResourceResolver resourceResolver = req.getResourceResolver();

        //Get request params
        RequestParameter rootPathParam = req.getRequestParameter("rootPath");
        RequestParameter pageParam = req.getRequestParameter("page");
        RequestParameter resultsPerPageParam = req.getRequestParameter("resultsPerPage");
        RequestParameter tagParam = req.getRequestParameter("tag");

        //set root path; if one was not responded send an error and exit
        String rootPath = rootPathParam.getString();
        if(StringUtils.isEmpty(rootPath)) {
            resp.sendError(400, "Bad request: missing root path");
            return;
        }

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

        //Verify and process resultsPerPage param
        int resultsPerPage = DEFAULT_ARTICLES_PER_PAGE;
        if (resultsPerPageParam != null) {
            try {
                if(Integer.parseInt(resultsPerPageParam.getString()) > 0) {
                    resultsPerPage = Integer.parseInt(resultsPerPageParam.getString());
                }
            } catch (NumberFormatException e) {
                log.error("TaggedArticlesServlet: unable to convert requested resultsPerPage to integer", e);
            }
        }

        //Verify and set tag param
        String tag = tagParam == null ? "" : tagParam.getString();

        //Build the articles query
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", "/content/solidigm");
        queryMap.put("type", "cq:Page");
        queryMap.put("group.p.or", "true");
        queryMap.put("group.1_group.1_property", "jcr:content/cq:template");
        queryMap.put("group.1_group.1_property.operation", "equals");
        queryMap.put("group.1_group.1_property.value", ARTICLE_TEMPLATE);
        queryMap.put("group.2_group.1_property", "jcr:content/cq:template");
        queryMap.put("group.2_group.1_property.operation", "equals");
        queryMap.put("group.2_group.1_property.value", EVENT_TEMPLATE);
        queryMap.put("orderby", "@jcr:content/date");
        queryMap.put("orderby.sort", "desc");
        queryMap.put("p.limit", String.valueOf(resultsPerPage));
        queryMap.put("p.offset", String.valueOf(resultsPerPage * (page - 1)));
        if(StringUtils.isNotEmpty(tag)) {
            queryMap.put("group.1_group.2_property", "jcr:content/articleCategory");
            queryMap.put("group.1_group.2_property.operation", "equals");
            queryMap.put("group.1_group.2_property.value", tag);
            queryMap.put("group.2_group.2_property", "jcr:content/articleCategory");
            queryMap.put("group.2_group.2_property.operation", "equals");
            queryMap.put("group.2_group.2_property.value", tag);
            queryMap.put("group.3_group.1_property", "jcr:content/cq:template");
            queryMap.put("group.3_group.1_property.operation", "equals");
            queryMap.put("group.3_group.1_property.value", ARTICLE_TEMPLATE);
            queryMap.put("group.3_group.2_property", "jcr:content/cq:tags");
            queryMap.put("group.3_group.2_property.operation", "equals");
            queryMap.put("group.3_group.2_property.value", tag);
            queryMap.put("group.4_group.1_property", "jcr:content/cq:template");
            queryMap.put("group.4_group.1_property.operation", "equals");
            queryMap.put("group.4_group.1_property.value", EVENT_TEMPLATE);
            queryMap.put("group.4_group.2_property", "jcr:content/cq:tags");
            queryMap.put("group.4_group.2_property.operation", "equals");
            queryMap.put("group.4_group.2_property.value", tag);
        }

        //Execute the query and evaluate the results
        Session session = resourceResolver.adaptTo(Session.class);
        SearchResult result = queryBuilder.createQuery(PredicateGroup.create(queryMap), session).getResult();
        long totalMatches = result.getTotalMatches();
        int totalPages = (int) Math.ceil((double) totalMatches / resultsPerPage);

        //Iterate through the hits, processing each article page and adding them to the JSON Array
        List<Hit> hits = result.getHits();
        JsonArray resultsArray = new JsonArray();
        for (Hit hit : hits) {
            try {
                String articlePath = hit.getPath();
                if(StringUtils.isNotEmpty(articlePath)) {
                    Resource articlePageResource = resourceResolver.getResource(articlePath.concat("/jcr:content"));
                    if (articlePageResource != null) {
                        //Get article page properties and add to json array
                        ArticlePage articlePage = articlePageResource.adaptTo(ArticlePage.class);
                        JsonObject resultProps = new JsonObject();
                        resultProps.addProperty("teaserImage", articlePage.getTeaserImage());
                        resultProps.addProperty("articleTitle", articlePage.getArticleTitle());
                        resultProps.addProperty("articleLink", articlePath.concat(".html"));

                        //Resolve category tag title
                        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
                        resultProps.addProperty("categoryTag",
                                                 tagManager.resolve(articlePage.getArticleCategory()).getTitle());

                        //Get alt text from DAM if it is not set for page
                        if(StringUtils.isEmpty(articlePage.getTeaserImageAltText())
                            && StringUtils.isNotEmpty(articlePage.getTeaserImage())) {
                            resultProps.addProperty("altText", ImageUtil.getAltTextFromDAM(resourceResolver,
                                                                                            articlePage.getTeaserImage()));
                        } else {
                            resultProps.addProperty("altText", articlePage.getTeaserImageAltText());
                        }

                        resultsArray.add(resultProps);
                    }
                }
            } catch (RepositoryException e) {
                log.error("Error adding article to response", e);
            }
        }

        //Build response JSON
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("page", page);
        responseObject.addProperty("resultsPerPage", resultsPerPage);
        responseObject.addProperty("tag", tag);
        responseObject.addProperty("totalMatches", totalMatches);
        responseObject.addProperty("totalPages", totalPages);
        responseObject.add("results", resultsArray);

        resp.setContentType("application/json");
        resp.getWriter().write(responseObject.toString());
    }
}
