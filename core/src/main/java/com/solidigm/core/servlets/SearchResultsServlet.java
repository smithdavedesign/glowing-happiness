package com.solidigm.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.solidigm.core.beans.SearchResultItem;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
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
 * Servlet that takes the root path, current page, results per page, and selected tag of the taggedArticles component.
 * It then returns any article pages matching that tag, offset by the current page * articles per page, only returns
 * an amount equal to articles per page of articles at a time.
 *
 * Parameters:
 * rootPath (Required) {String}: the localized root path of the site to search on
 * currentPage (Default: 1) {int}: the index of the current page of results
 * resultsPerPage (Default: 10) {int}: the number of results to display on a single page of results
 * query (Optional) {String}: the content to search the site on
 *
 * Returns:
 * {
 *     "totalItems": 102,
 *     "items": [
 *         {
 *             "title": "Test Title",
 *             "url": "https://wwww.google.com",
 *             "description": "Meta page description: Lorem ipsum dolor sit amet.",
 *             "tags": [
 *                  {
 *                      "text": "tag1", "url": "https://tag1.com"
 *                  }
 *             ],
 *             "image": {
 *                 "url": "https://picsum.photos/500/500",
 *                 "altText": "Some Alt text"
 *             }
 *         }
 *     ]
 * }
 */
@Component(service = { Servlet.class })
@SlingServletPaths("/bin/solidigm/search")
@ServiceDescription("Search Results Servlet")
public class SearchResultsServlet extends SlingSafeMethodsServlet {
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
        RequestParameter pageParam = req.getRequestParameter("currentPage");
        RequestParameter resultsPerPageParam = req.getRequestParameter("resultsPerPage");
        RequestParameter queryParam = req.getRequestParameter("query");

        //set root path; if one was not provided send an error and exit
        if(rootPathParam == null || StringUtils.isEmpty(rootPathParam.getString())) {
            resp.sendError(400, "Bad request: missing root path");
            return;
        }
        String rootPath = rootPathParam.getString();

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

        //Verify and set query param
        if(queryParam == null || StringUtils.isEmpty(queryParam.getString())) {
            resp.sendError(400, "Bad request: missing query");
            return;
        }
        String queryText = queryParam.getString();

        //Build the search query
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", rootPath);
        queryMap.put("type", "cq:Page");
        queryMap.put("fulltext", queryText);
        queryMap.put("group.p.or", "true");
        queryMap.put("group.1_property", "jcr:content/hideInNav");
        queryMap.put("group.1_property.operation", "unequals");
        queryMap.put("group.1_property.value", "true");
        queryMap.put("group.2_property", "jcr:content/hideInNav");
        queryMap.put("group.2_property.operation", "exists");
        queryMap.put("group.2_property.value", "false");
        queryMap.put("p.limit", String.valueOf(resultsPerPage));
        queryMap.put("p.offset", String.valueOf(resultsPerPage * (page - 1)));

        //Execute the query and evaluate the results
        Session session = resourceResolver.adaptTo(Session.class);
        SearchResult result = queryBuilder.createQuery(PredicateGroup.create(queryMap), session).getResult();
        long totalMatches = result.getTotalMatches();

        //Iterate through the hits, processing each search result and adding them to the JSON Array
        List<Hit> hits = result.getHits();
        JsonArray resultsArray = new JsonArray();
        for (Hit hit : hits) {
            try {
                SearchResultItem resultPage = new SearchResultItem(hit.getPath(), req.getResourceResolver());
                JsonObject resultObject = new JsonObject();

                //Serialize search result to JSON
                resultObject.addProperty("title", resultPage.getTitle());
                resultObject.addProperty("description", resultPage.getDescription());
                resultObject.addProperty("url", resultPage.getUrl());
                resultObject.addProperty("urlLabel", resourceResolver.map(hit.getPath()));

                JsonObject imageProps = new JsonObject();
                imageProps.addProperty("url", resultPage.getImage());
                imageProps.addProperty("altText", resultPage.getAltText());
                resultObject.add("image", imageProps);

                resultsArray.add(resultObject);
            } catch (RepositoryException e) {
                log.error("Error adding search result to response", e);
            }
        }

        //Build response JSON
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("totalItems", totalMatches);
        responseObject.add("items", resultsArray);
        resp.setContentType("application/json");
        resp.getWriter().write(responseObject.toString());
    }
}
