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
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = { Servlet.class })
@SlingServletPaths("/bin/solidigm/signOut")
@ServiceDescription("Sign Out Servlet")
public class SignOutServlet extends SlingSafeMethodsServlet {
    @Reference
    private Authenticator slingAuthenticator;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {
        this.slingAuthenticator.logout(req, resp);
        if (req.getSession(false) != null) {
            req.getSession().invalidate();
        }
        resp.sendRedirect("/");
    }
}
