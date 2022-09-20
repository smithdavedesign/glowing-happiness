package com.solidigm.core.servlets;

import com.google.gson.JsonObject;
import com.solidigm.core.beans.ProductModel;
import com.solidigm.core.beans.ProductPage;
import com.solidigm.core.beans.ProductVariation;
import com.solidigm.core.services.ProductService;
import com.solidigm.core.utils.SolidigmUtils;
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

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Servlet that takes the path of the PDP pages of the products to compare and the variation IDs of the products to
 * compare and returns information about those product variations to be displayed in the product comparison overlay
 *
 * Returns:
 * productPagePath {String}: copy of what was sent
 * variation {String}: copy of what was sent
 * name {String}: name of the product
 * image {String}: image for the product
 * altText {String}: alt text for the product image
 * productInterface {String}
 * formFactor {String}
 * capacity {String}
 */
@Component(service = { Servlet.class })
@SlingServletPaths("/bin/solidigm/productConfigurator")
@ServiceDescription("Product Configurator Servlet")
public class ProductConfiguratorServlet extends SlingSafeMethodsServlet {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Reference
    private ProductService productService;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        ResourceResolver resourceResolver = req.getResourceResolver();
        String response = new String(resourceResolver.getResource("/var/configurator.json").adaptTo(InputStream.class).readAllBytes());
        resp.setContentType("application/json");
        resp.getWriter().write(response);
    }
}
