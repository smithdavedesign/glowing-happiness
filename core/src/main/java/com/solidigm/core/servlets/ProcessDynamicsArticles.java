package com.solidigm.core.servlets;

import com.solidigm.core.services.DynamicsArticlesService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
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
    DynamicsArticlesService dynamicsArticlesService;

    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException {
        String accessToken = dynamicsArticlesService.acquireToken();

        if(accessToken!=null) {
            response.getWriter().println("Successfully established the connection with Token number: " + accessToken);

            String articleJson = dynamicsArticlesService.fetchArticles(accessToken);
            if(articleJson!=null) {
                response.getWriter().println("Received these knowledge articles:");
                response.getWriter().println(articleJson);
                dynamicsArticlesService.processArticles(articleJson);
                response.getWriter().println("Successfully processed Dynamics article pages");
            }
        }
    }
}