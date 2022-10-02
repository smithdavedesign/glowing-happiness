package com.solidigm.core.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.solidigm.core.beans.TileData;
import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
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

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * Servlet that takes the path of a dynamic tile list component and returns the json structure of tiles the currently
 * signed-in user can see based on group membership
 *
 * Parameters:
 * path (Required) {String}: the path to the dynamic tile list component
 *
 * Returns:
 * JSON of the following structure:
 * [
 *     {
 *         "title": "Sample Title",
 *         "icon": "diamond",
 *         "description": "sample tile description",
 *         "link": {
 *             "label": "Sample CTA",
 *             "target": "_blank",
 *             "link": "https://example.com",
 *             "linkType": "solidigm-btn--external"
 *         }
 *     },
 *     {
 *         "title": "Sample Title",
 *         "icon": "diamond",
 *         "description": "sample tile description",
 *         "link": {
 *             "label": "Sample CTA",
 *             "target": "_blank",
 *             "link": "https://example.com",
 *             "linkType": "solidigm-btn--external"
 *         }
 *     },
 *     {...}
 * ]
 */
@Component(service = { Servlet.class })
@SlingServletPaths("/bin/solidigm/dynamicTilelist")
@ServiceDescription("Returns tiles that the user can see based on user group membership.")
public class DynamicTileListServlet extends SlingSafeMethodsServlet {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Returns a JsonObject with data from the tile provided in this structure:
     * {
     *     "title": "Example Title",
     *     "icon": "diamond",
     *     "description": "tile description",
     *     "link": {
     *         "label": "Example CTA",
     *         "target": "_blank",
     *         "link": "https://example.com",
     *         "linkType": "solidigm-btn--external"
     *     }
     * }
     * @param tile {TileData}
     * @return JsonObject
     */
    private JsonObject tileToJson(TileData tile) {
        JsonObject ret = new JsonObject();
        ret.addProperty("title", tile.getTitle());
        ret.addProperty("icon", tile.getIcon());
        ret.addProperty("description", tile.getDescription());
        JsonObject link = new JsonObject();
        link.addProperty("label", tile.getLabel());
        link.addProperty("target", tile.getTarget());
        link.addProperty("link", tile.getLink());
        link.addProperty("linkType", tile.getLinkType());
        ret.add("link", link);
        return ret;
    }

    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {
        //Get request params
        RequestParameter tileListPathParam = req.getRequestParameter("path");

        //Get tile list path parameter, if it wasn't sent or is empty send an error.
        if(tileListPathParam == null || StringUtils.isEmpty(tileListPathParam.getString())) {
            resp.sendError(400, "Bad request: missing path");
            return;
        }
        String tileListPath = tileListPathParam.getString();

        //Get the groups the current user is a member of
        ResourceResolver resourceResolver = req.getResourceResolver();
        Session session = req.getResourceResolver().adaptTo(Session.class);
        UserManager userManager = resourceResolver.adaptTo(UserManager.class);
        if (session == null || userManager == null) {
            resp.sendError(400, "Bad request: missing session params");
            return;
        }
        String userID = session.getUserID();
        Set<String> groups = new HashSet<>();
        if(StringUtils.isNotEmpty(userID) && !userID.equals("anonymous")) {
            try {
                Authorizable auth = userManager.getAuthorizable(userID);
                auth.memberOf().forEachRemaining(group -> {
                    try {
                        groups.add(group.getID());
                    } catch (RepositoryException e) {
                        log.error("Error in Dynamic Tile List: Couldn't get groupID of user group", e);
                    }
                });
            } catch (RepositoryException e) {
                log.error("Error in Dynamic Tile List: Couldn't get signed in user.", e);
                return;
            }
        }

        //Populate the list of tiles with tiles that are shown to the current user's groups and tiles that are shown to
        //everyone
        JsonArray responseJson = new JsonArray();
        Resource tileList = resourceResolver.getResource(tileListPath);
        if(tileList != null) {
            Resource tiles = tileList.getChild("tileInfo");
            if (tiles != null) {
                for (Resource tile : tiles.getChildren()) {
                    TileData tileData = tile.adaptTo(TileData.class);
                    String[] tileGroups = tile.getValueMap().get("groups", String[].class);
                    if (tileData != null && tileGroups != null) {
                        Set<String> visibleTo = new HashSet<>(
                                Arrays.asList(tileGroups));
                        if (visibleTo.contains("everyone")) {
                            responseJson.add(tileToJson(tileData));
                        } else {
                            visibleTo.retainAll(groups);
                            if (!visibleTo.isEmpty()) {
                                responseJson.add(tileToJson(tileData));
                            }
                        }
                    }
                }
            }
        }

        //Write tile data to response
        resp.setContentType("application/json");
        resp.getWriter().write(responseJson.toString());
    }
}
