package com.solidigm.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.solidigm.core.beans.OrderingSpec;
import com.solidigm.core.beans.ProductModel;
import com.solidigm.core.beans.ProductVariation;
import com.solidigm.core.services.ProductService;
import com.solidigm.core.utils.SolidigmUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Servlet that takes the path of a PDP page and returns the data of all of that products variations to be displayed
 * in the product configurator on that page
 *
 * Parameters:
 * path (Required) {String}: the path to the PDP page for this product
 *
 * Returns:
 * JSON of the following structure:
 * {
 *     "allFilters": {
 *          "interface": [
 *              "Interface 1",
 *              "Interface 2",
 *              "..."
 *          ],
 *          "formFactor": [
 *              "Form Factor 1",
 *              "Form Factor 2",
 *              "..."
 *          ],
 *          "capacity": [
 *              "Capacity 1",
 *              "Capacity 2",
 *              "..."
 *          ]
 *      },
 *      "filters": [
 *          {
 *              "interface": "Interface 1",
 *              "formFactor": "Form Factor 1",
 *              "capacity": "Capacity 2",
 *              "result": 0
 *          },
 *          {
 *              "interface": "Interface 1",
 *              "formFactor": "Form Factor 2",
 *              "capacity": "Capacity 1",
 *              "result": 1
 *          },
 *          {...}
 *      ],
 *      "results: [
 *          {
 *              "path":0,
 *              "productPagePath":"/test/testPage",
 *              "attributes": {
 *                  "CertifiedUseConditions":"",
 *                  "DriveCapacity":"",
 *                  "EnduranceDWPD":"",
 *                  "EndurancePBW":"",
 *                  "FilterUseConditions":"",
 *                  "FIPSVersion":""
 *                  "LatencyRandomRead":"",
 *                  "LatencyRandomWriteValue":"",
 *                  "LatencySequentialReady":"",
 *                  "LatencySequentialWriteValue":"",
 *                  "LithographyType":"",
 *                  "MTBF":"",
 *                  "NANDGeneration":"",
 *                  "NANDLayerCount":"",
 *                  "NANDTechnology":"",
 *                  "NANDType":"",
 *                  "NVMeVersion":"",
 *                  "OCPVersion":"",
 *                  "OpalVersion":"",
 *                  "OperatingTemperature":"",
 *                  "ProductGroup":"",
 *                  "RandomRead100PctSpanValue":"",
 *                  "RandomWrite100PctSpanValue":"",
 *                  "SequentialReadMB":"",
 *                  "SequentialWriteMB":"",
 *                  "SSDEnduranceRating":"",
 *                  "SSDFormFactor":"",
 *                  "SSDInterface":"",
 *                  "SSDPowerActive":"",
 *                  "SSDPowerIdle":"",
 *                  "SSDShock":"",
 *                  "SSDWeight":"",
 *                  "UBER":"",
 *                  "VibrationNonOperating":"",
 *                  "VibrationOperating":"",
 *                  "WarrantyLengthYears":""
 *              },
 *              "orderingSpecs": [
 *                  {
 *                      "name":"",
 *                      "eccn":"",
 *  *                   "ccats":"",
 *  *                   "ushts":""
 *                      "orderingCode":"",
 *                      "shippingMedia":""
 *                  },
 *                  {...}
 *              ]
 *          },
 *          {...}
 *      ]
 * }
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
        //Get request params
        RequestParameter pathParam = req.getRequestParameter("path");

        //Get tile list path parameter, if it wasn't sent or is empty send an error.
        if(pathParam == null || StringUtils.isEmpty(pathParam.getString())) {
            resp.sendError(400, "Bad request: missing path");
            return;
        }
        String path = pathParam.getString();

        //Get product data of product at provided path
        ProductModel product = productService.getProduct(SolidigmUtils.getProductCategoryFromPath(path),
                                                         SolidigmUtils.getProductSeriesFromPath(path),
                                                         SolidigmUtils.getProductNameFromPath(path));

        //Serialized product data to JSON structure
        Gson gson = new Gson();
        List<String> interfaces = new ArrayList<>();
        List<String> formFactors = new ArrayList<>();
        List<String> capacities = new ArrayList<>();
        JsonArray filters = new JsonArray();
        JsonArray results = new JsonArray();
        if (product != null) {
            Map<String, ProductVariation> variations = product.getVariations();

            //Loop through all product variations
            for (Map.Entry<String, ProductVariation> entry : variations.entrySet()) {
                String variationID = entry.getKey();
                ProductVariation variation = entry.getValue();

                //Get unique product interfaces, form factors, and capacities
                if (!interfaces.contains(variation.getProductInterface())) {
                    interfaces.add(variation.getProductInterface());
                }
                if (!formFactors.contains(variation.getFormFactor())) {
                    formFactors.add(variation.getFormFactor());
                }
                if (!capacities.contains(variation.getCapacity())) {
                    capacities.add(variation.getCapacity());
                }

                //Serialize data matching this variation to a combination of interface, capacity and form factor
                JsonObject variationKey = new JsonObject();
                variationKey.addProperty("interface", variation.getProductInterface());
                variationKey.addProperty("formFactor", variation.getFormFactor());
                variationKey.addProperty("capacity", variation.getCapacity());
                variationKey.addProperty("result", variationID);
                filters.add(variationKey);

                //Serialize product attributes
                JsonObject variationValue = new JsonObject();
                JsonObject variationAttributes = gson.fromJson(variation.getAttributesJSON(), JsonObject.class);
                JsonArray orderingSpecs = new JsonArray();
                variationValue.addProperty("path", variationID);
                variationValue.addProperty("productPagePath", path);
                variationValue.add("attributes", variationAttributes);
                for (OrderingSpec spec : variation.getOrderingInfo()) {
                    JsonObject specJSON = new JsonObject();
                    specJSON.addProperty("name", spec.getName());
                    specJSON.addProperty("eccn", spec.getEccn());
                    specJSON.addProperty("ccats", spec.getCcats());
                    specJSON.addProperty("ushts", spec.getUshts());
                    specJSON.addProperty("orderingCode", spec.getOrderingCode());
                    specJSON.addProperty("shippingMedia", spec.getShippingMedia());
                    orderingSpecs.add(specJSON);
                }
                variationValue.add("orderingSpecs", orderingSpecs);
                results.add(variationValue);
            }
        }

        //Create JSON data for all unique interfaces, form factors and capacities
        JsonObject allFilters = new JsonObject();
        allFilters.add("interface", gson.toJsonTree(interfaces));
        allFilters.add("formFactor", gson.toJsonTree(formFactors));
        allFilters.add("capacity", gson.toJsonTree(capacities));

        //Write response to JSON
        JsonObject respJSON = new JsonObject();
        respJSON.add("allFilters", allFilters);
        respJSON.add("filters", filters);
        respJSON.add("results", results);
        resp.setContentType("application/json");
        resp.getWriter().write(respJSON.toString());
    }
}
