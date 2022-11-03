package com.solidigm.core.servlets;

import com.solidigm.core.services.PDPConversionService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = { Servlet.class })
@SlingServletPaths("/bin/solidigm/convertPages")
@ServiceDescription("Convert PDP Pages Servlet")
public class ConvertPDPServlet extends SlingSafeMethodsServlet {

    @Reference
    private PDPConversionService pdpConversion;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws IOException {
        pdpConversion.convertPDP();
        String responseCheck = pdpConversion.checkPages();
        resp.getWriter().println(responseCheck);
    }
}
