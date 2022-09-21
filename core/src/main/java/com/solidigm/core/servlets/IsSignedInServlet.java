package com.solidigm.core.servlets;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = { Servlet.class })
@SlingServletPaths("/bin/solidigm/isSignedIn")
@ServiceDescription("Is Signed In Servlet")
public class IsSignedInServlet extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {
        //Get current user
        Session session = req.getResourceResolver().adaptTo(Session.class);
        String userID = session.getUserID();

        //Check if user is anonymous (not signed in) and write the response
        resp.setContentType("application/json");
        if(StringUtils.isNotEmpty(userID) && !userID.equals("anonymous")) {
            resp.getWriter().write("{ \"isSignedIn\": true }");
        } else {
            resp.getWriter().write("{ \"isSignedIn\": false }");
        }
    }
}
