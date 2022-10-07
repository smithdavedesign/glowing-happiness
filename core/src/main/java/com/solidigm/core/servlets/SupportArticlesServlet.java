package com.solidigm.core.servlets;

import com.day.cq.wcm.api.Page;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;


/**
 * Servlet that takes the path of the support index page and returns the list of knowledge articles that are its
 * children
 *
 * Parameters:
 * path (Required) {String}: the path to the support index page to grab articles below
 *
 * Returns:
 * {
 *     "totalItems": 12,
 *     "items": [
 *         {
 *              "title": "Sample Title",
 *              "url": "/sample/path",
 *              "models": ["model 1, model 7]
 *         },
 *         {...}
 *     ]
 * }
 */
@Component(service = { Servlet.class })
@SlingServletPaths("/bin/solidigm/supportArticlesServlet")
@ServiceDescription("Support Articles Servlet")
public class SupportArticlesServlet extends SlingSafeMethodsServlet {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        ResourceResolver resourceResolver = req.getResourceResolver();

        //Get request params
        RequestParameter pathParam = req.getRequestParameter("path");

        //Get page path parameter, if it wasn't send an error
        if(pathParam == null || StringUtils.isEmpty(pathParam.getString())) {
            resp.sendError(400, "Bad request: missing page path");
            return;
        }
        String path = pathParam.getString();

        //Resolve path to page resource, if it doesn't resolve send an error
        Resource pageResource = null;
        Page page = null;
        pageResource = resourceResolver.getResource(path);
        if (pageResource == null) {
            resp.sendError(406, "Not Acceptable: Requested support index page does not exist");
            return;
        }

        page = pageResource.adaptTo(Page.class);
        if (page == null) {
            resp.sendError(406, "Not Acceptable: Requested support index page does not exist");
            return;
        }

        //Iterate through child pages and build json
        JsonArray pages = new JsonArray();
        for (Iterator<Page> it = page.listChildren(); it.hasNext(); ) {
            Page currentPage = it.next();
            JsonObject pageJSON = new JsonObject();
            pageJSON.addProperty("title", currentPage.getTitle());
            pageJSON.addProperty("url", currentPage.getPath());
            pages.add(pageJSON);
        }

        //Build response JSON
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("totalItems", pages.size());
        responseObject.add("items", pages);
        resp.setContentType("application/json");
        resp.getWriter().write(responseObject.toString());
    }
}
