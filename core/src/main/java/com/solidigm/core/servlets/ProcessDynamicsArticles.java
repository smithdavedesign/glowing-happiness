package com.solidigm.core.servlets;

//import com.microsoft.aad.msal4j.IAuthenticationResult;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = { Servlet.class })
@SlingServletPaths("/bin/solidigm/dynamicArticles")
@ServiceDescription("Process Dynamics Articles Servlet")
public class ProcessDynamicsArticles extends SlingAllMethodsServlet {


    /** The Constant serialVersionUID */
    private static final long serialVersionUID = 6012190046250664862L;

    /** The Constant LOG */
    protected static final Logger LOG = LoggerFactory.getLogger(ProcessDynamicsArticles.class);

    @Reference
    com.solidigm.core.services.ProcessDynamicsArticles processDynamicsArticles;

    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException {

        try {
            String accessToken = processDynamicsArticles.aquireToken();

            if(accessToken!=null) {
                response.getWriter().println("Successfully established the connection with Token number: " + accessToken);

                String articleJson = processDynamicsArticles.fetchArticles(accessToken);
                if(articleJson!=null) {
                    processDynamicsArticles.processArticles(articleJson);
                    response.getWriter().println("Successfully processed Dynamics article pages");
                }
            }
        } catch (IOException | LoginException e) {
            response.getWriter().println("Issue in processing the Dynamics articles. Please check logs for more details");
            LOG.error("Exception", e);
        }
    }
}