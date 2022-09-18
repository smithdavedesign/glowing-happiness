package com.solidigm.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.TagManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.solidigm.core.beans.ArticlePage;
import com.solidigm.core.beans.ProductModel;
import com.solidigm.core.beans.ProductPage;
import com.solidigm.core.beans.ProductVariation;
import com.solidigm.core.services.ProductService;
import com.solidigm.core.utils.ImageUtil;
import com.solidigm.core.utils.SolidigmUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
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
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.solidigm.core.utils.SolidigmConstants.ARTICLE_TEMPLATE;
import static com.solidigm.core.utils.SolidigmConstants.EVENT_TEMPLATE;

/**
 * Servlet that takes the path of the PDP pages of the products to compare and the variation IDs of the products to
 * compare and returns information about those product variations to be displayed in the product comparison overlay
 *
 * Parameters:
 * productPagePath (Required) {String}: the path to the PDP page of the product to compare
 * variation (Required) {String}: the variation ID of the product to compare
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
@SlingServletPaths("/bin/solidigm/productComparisonOverlay")
@ServiceDescription("Product Comparison Overlay Servlet")
public class ProductComparisonOverlayServlet extends SlingSafeMethodsServlet {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Reference
    private ProductService productService;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        ResourceResolver resourceResolver = req.getResourceResolver();

        //Get request params
        RequestParameter productPagePathParam = req.getRequestParameter("productPagePath");
        RequestParameter variationParam = req.getRequestParameter("variation");

        //Get product page path parameter, if it wasn't send an error
        if(productPagePathParam == null || StringUtils.isEmpty(productPagePathParam.getString())) {
            resp.sendError(400, "Bad request: missing product page path");
            return;
        }
        String productPagePath = productPagePathParam.getString();

        //Get variation parameter, if it wasn't provided send an error
        if(variationParam == null || StringUtils.isEmpty(variationParam.getString())) {
            resp.sendError(400, "Bad request: missing product variation");
            return;
        }
        String variation = variationParam.getString();

        //Resolve product data, if it does not exist send an error
        ProductModel productModel = productService.getProduct(SolidigmUtils.getProductCategoryFromPath(productPagePath),
                                                              SolidigmUtils.getProductSeriesFromPath(productPagePath),
                                                              SolidigmUtils.getProductNameFromPath(productPagePath));
        if (productModel == null) {
            resp.sendError(406, "Not Acceptable: Requested product data does not exist");
            return;
        }

        ProductPage productPage = new ProductPage(productPagePath, resourceResolver, productService);
        ProductVariation productVariation = productModel.getVariations().get(variation);

        //Build response JSON
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("productPagePath", productPagePath);
        responseObject.addProperty("variation", variation);
        responseObject.addProperty("name", productPage.getName());
        responseObject.addProperty("image", productPage.getImage());
        responseObject.addProperty("altText", productPage.getAltText());
        responseObject.addProperty("productInterface", productVariation.getProductInterface());
        responseObject.addProperty("formFactor", productVariation.getFormFactor());
        responseObject.addProperty("capacity", productVariation.getCapacity());
        resp.setContentType("application/json");
        resp.getWriter().write(responseObject.toString());
    }
}
