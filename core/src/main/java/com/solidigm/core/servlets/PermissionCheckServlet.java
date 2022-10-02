package com.solidigm.core.servlets;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.auth.Authenticator;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import javax.servlet.Servlet;
import javax.jcr.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component(service = { Servlet.class })
@SlingServletPaths("/bin/solidigm/permissionCheck")
@ServiceDescription("Permission Check Servlet")
public class PermissionCheckServlet extends SlingSafeMethodsServlet {
    @Reference
    private Authenticator slingAuthenticator;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void doHead(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try{
            //retrieve the requested URL
            String uri = request.getParameter("uri");
            //obtain the session from the request
            Session session = request.getResourceResolver().adaptTo(javax.jcr.Session.class);
            if (session == null) {
                response.sendError(400, "400 Bad Request: missing user session.");
                return;
            }
            //perform the permissions check
            try {
             session.checkPermission(uri, Session.ACTION_READ);
             logger.info("AuthChecker successful");
             response.setStatus(SlingHttpServletResponse.SC_OK);
            } catch(Exception e) {
             logger.info("AuthChecker access denied!");
             response.setStatus(SlingHttpServletResponse.SC_FORBIDDEN);
            }
        }catch(Exception e){
            logger.error("AuthChecker servlet exception: " + e.getMessage());
        }
        }
}
