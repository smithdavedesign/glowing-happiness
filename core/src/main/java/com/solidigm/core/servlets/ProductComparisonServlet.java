package com.solidigm.core.servlets;

import com.google.gson.JsonArray;
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
import java.util.Map;


/**
 * Servlet that takes the path of the PDP pages of the products to compare and the variation IDs of the products to
 * compare and returns information about those product variations to be displayed in the product comparison overlay
 *
 * Parameters:
 * productPagePath (Required) {String}: the path to the PDP page of the product to compare
 *
 * Returns:
 * {
 *     title: "D7-P5600",
 *     image: "/assets/product-image.png",
 *     altText: "Sample Alt Text",
 *     variants: [
 *         {
 *              variationID: "12345",
 *              specsList: [
 *                  "Capacity",
 *                  "Form Factor",
 *                  "Interface",
 *                  "NAND Technology",
 *                  "Sequential Read",
 *                  "Sequentail Write",
 *                  "Random Read",
 *                  "Random Write",
 *                  "Latency Read",
 *                  "Latency Write",
 *                  "Power Active",
 *                  "Power Idle",
 *                  "Warranty length",
 *                  "endurance rating",
 *                  "MTBF",
 *                  "UBER",
 *                  "Vibration Operating",
 *                  "Vibration non Operating",
 *                  "Shock",
 *                  "Operating Temperature",
 *                  "Hardware encryption",
 *                  "Advanced power loss data protection",
 *                  "High endurance technology"
 *             ]
 *         },
 *         {...}
 *     ]
 * }
 */
@Component(service = { Servlet.class })
@SlingServletPaths("/bin/solidigm/productComparison")
@ServiceDescription("Product Comparison Servlet")
public class ProductComparisonServlet extends SlingSafeMethodsServlet {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Reference
    private ProductService productService;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        ResourceResolver resourceResolver = req.getResourceResolver();

        //Get request params
        RequestParameter productPagePathParam = req.getRequestParameter("path");

        //Get product page path parameter, if it wasn't send an error
        if(productPagePathParam == null || StringUtils.isEmpty(productPagePathParam.getString())) {
            resp.sendError(400, "Bad request: missing product page path");
            return;
        }
        String productPagePath = productPagePathParam.getString();

        //Resolve product data, if it does not exist send an error
        ProductModel productModel = productService.getProduct(SolidigmUtils.getProductCategoryFromPath(productPagePath),
                                                              SolidigmUtils.getProductSeriesFromPath(productPagePath),
                                                              SolidigmUtils.getProductNameFromPath(productPagePath));
        if (productModel == null) {
            resp.sendError(406, "Not Acceptable: Requested product data does not exist");
            return;
        }

        //Iterate through variants of product model, listing each variations specs
        JsonArray variants = new JsonArray();
        for (Map.Entry<String, ProductVariation> variationEntry : productModel.getVariations().entrySet()) {
            String variationID = variationEntry.getKey();
            ProductVariation variation = variationEntry.getValue();
            JsonObject variationJSON = new JsonObject();
            variationJSON.addProperty("variationID", variationID);

            //Write the list of specs for this variant in the order displayed on the comparison page
            JsonArray specsList = new JsonArray();
            specsList.add(variation.getCapacity());
            specsList.add(variation.getFormFactor());
            specsList.add(variation.getProductInterface());
            specsList.add(variation.getNandTechnology());
            specsList.add(variation.getSequentialRead());
            specsList.add(variation.getSequentialWrite());
            specsList.add(variation.getRandomRead());
            specsList.add(variation.getRandomWrite());
            specsList.add(variation.getSequentialReadLatency());
            specsList.add(variation.getSequentialWriteLatency());
            specsList.add(variation.getPowerActive());
            specsList.add(variation.getPowerIdle());
            specsList.add(variation.getWarrantyLength());
            specsList.add(variation.getEndurance());
            specsList.add(variation.getMtbf());
            specsList.add(variation.getUber());
            specsList.add(variation.getVibrationOperating());
            specsList.add(variation.getVibrationNonOperating());
            specsList.add(variation.getShock());
            specsList.add(variation.getOperatingTemperature());
            specsList.add(variation.getHardwareEncryption());
            specsList.add(variation.getDataProtection());
            specsList.add(variation.getHet());
            variationJSON.add("specsList", specsList);
            variants.add(variationJSON);
        }

        //Build response JSON
        ProductPage productPage = new ProductPage(productPagePath, resourceResolver, productService);
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("title", productPage.getName());
        responseObject.addProperty("image", productPage.getImage());
        responseObject.addProperty("altText", productPage.getAltText());
        responseObject.add("variants", variants);
        resp.setContentType("application/json");
        resp.getWriter().write(responseObject.toString());
    }
}
