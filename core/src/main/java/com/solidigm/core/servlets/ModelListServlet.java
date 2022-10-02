package com.solidigm.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.solidigm.core.beans.CapacitySortable;
import com.solidigm.core.beans.ProductModel;
import com.solidigm.core.beans.ProductPage;
import com.solidigm.core.services.ProductService;
import com.solidigm.core.utils.SolidigmConstants;
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

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;

/**
 * Servlet that takes the path of a Product Category Page or Product Series Page and returns the data of all of the
 * products models that are children or grand children of that page and data to filter on them
 *
 * Parameters:
 * path (Required) {String}: the path to the page the Model List is on
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
 *              "products": [
 *                  "P5510",
 *                  "D7-660p",
 *                  ...
 *              ]
 *          },
 *          {
 *              "interface": "Interface 1",
 *              "formFactor": "Form Factor 2",
 *              "capacity": "Capacity 1",
 *              "products": [
 *                  "D7-660p",
 *                  "D3-S7780",
 *                  ...
 *              ]
 *          },
 *          {...}
 *      ],
 *      "results: [
 *          {
 *              "image": "",
 *              "imageAlt": "",
 *              "title: "D7-660p",
 *              "description: "",
 *              "interface": "Interface 1",
 *              "formFactor: "Form Factor 1 / Form Factor 2",
 *              "capacity": "Capacity 1 - Capacity 2",
 *              "configureURL": "/sample/PDP.html#configurator",
 *              "detailsURL": "/sample/PDP.html"
 *          },
 *          {...}
 *      ]
 * }
 */
@Component(service = { Servlet.class })
@SlingServletPaths("/bin/solidigm/modelList")
@ServiceDescription("Model List Servlet")
public class ModelListServlet extends SlingSafeMethodsServlet {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Reference
    private ProductService productService;

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        //Get request params
        RequestParameter pathParam = req.getRequestParameter("path");

        //Get path parameter, if it wasn't sent or is empty send an error.
        if(pathParam == null || StringUtils.isEmpty(pathParam.getString())) {
            resp.sendError(400, "Bad request: missing path");
            return;
        }
        String path = pathParam.getString();

        //Create and execute query on descendants of this page that are PDP pages
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", path);
        queryMap.put("type", "cq:Page");
        queryMap.put("property", "jcr:content/cq:template");
        queryMap.put("property.operation", "equals");
        queryMap.put("property.value", SolidigmConstants.PDP_TEMPLATE);
        ResourceResolver resourceResolver = req.getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);
        SearchResult result = queryBuilder.createQuery(PredicateGroup.create(queryMap), session).getResult();

        //Create Maps of interfaces, form factors, and capacities with their corresponding set of products.
        Map<String, HashSet<String>> interfaces = new HashMap<>();
        interfaces.put("Any Interface", new HashSet<>());
        Map<String, HashSet<String>> formFactors = new HashMap<>();
        formFactors.put("Any Form Factor", new HashSet<>());
        Map<String, HashSet<String>> capacities = new HashMap<>();
        capacities.put("Any Capacity", new HashSet<>());
        HashMap<String, CapacitySortable> capacitiesSortable = new HashMap<>();
        JsonArray products = new JsonArray();
        for (Hit hit : result.getHits()) {
            try {
                //Resolve hit to product data and product teaser data
                ProductModel productData = productService.getProduct(SolidigmUtils.getProductCategoryFromPath(hit.getPath()),
                                                                 SolidigmUtils.getProductSeriesFromPath(hit.getPath()),
                                                                 SolidigmUtils.getProductNameFromPath(hit.getPath()));
                ProductPage productPage = new ProductPage(hit.getPath(), resourceResolver, productService);

                if (productData != null) {
                    //Populate interface, form factor, and capacity maps with the information from this product.
                    for (String productInterface : productData.getInterfaces()) {
                        if (!interfaces.containsKey(productInterface)) {
                            interfaces.put(productInterface, new HashSet<>());
                        }
                        interfaces.get("Any Interface").add(productPage.getName());
                        interfaces.get(productInterface).add(productPage.getName());
                    }
                    for (String formFactor : productData.getFormFactors()) {
                        if (!formFactors.containsKey(formFactor)) {
                            formFactors.put(formFactor, new HashSet<>());
                        }
                        formFactors.get("Any Form Factor").add(productPage.getName());
                        formFactors.get(formFactor).add(productPage.getName());
                    }
                    for (String capacity : productData.getCapacities()) {
                        if (!capacities.containsKey(capacity)) {
                            capacities.put(capacity, new HashSet<>());
                        }
                        capacities.get("Any Capacity").add(productPage.getName());
                        capacities.get(capacity).add(productPage.getName());
                    }
                    for (CapacitySortable capacitySortable : productData.getCapacitiesSortable()) {
                        capacitiesSortable.put(capacitySortable.getCapacity(),
                                               new CapacitySortable(capacitySortable.getRawCapacity(),
                                                                    capacitySortable.getCapacity()));
                    }
                }

                //Serialize product data to JSON
                JsonObject productJSON = new JsonObject();
                productJSON.addProperty("image", productPage.getImage());
                productJSON.addProperty("imageAlt", productPage.getAltText());
                productJSON.addProperty("title", productPage.getName());
                productJSON.addProperty("description", productPage.getDescription());
                productJSON.addProperty("interface", productPage.getProductInterface());
                productJSON.addProperty("formFactor", productPage.getFormFactor());
                productJSON.addProperty("capacity", productPage.getCapacity());
                productJSON.addProperty("configureURL", productPage.getProductPageLink().getLink().concat("#configurator"));
                productJSON.addProperty("detailsURL", productPage.getProductPageLink().getLink());
                products.add(productJSON);
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
        }

        //Create entries for every combination of form factor, interface, and capacity with the corresponding products
        Gson gson = new Gson();
        JsonArray filters = new JsonArray();
        for (Map.Entry<String, HashSet<String>> interfaceEntry : interfaces.entrySet()) {
            String productInterface = interfaceEntry.getKey();
            HashSet<String> interfaceSet = interfaceEntry.getValue();
            for (Map.Entry<String, HashSet<String>> formFactorEntry : formFactors.entrySet()) {
                String formFactor = formFactorEntry.getKey();
                HashSet<String> formFactorSet = formFactorEntry.getValue();
                for (Map.Entry<String, HashSet<String>> capacityEntry : capacities.entrySet()) {
                    String capacity = capacityEntry.getKey();
                    HashSet<String> capacitySet = (HashSet<String>) capacityEntry.getValue().clone();
                    capacitySet.retainAll(formFactorSet);
                    capacitySet.retainAll(interfaceSet);
                    JsonObject filter = new JsonObject();
                    filter.addProperty("interface", productInterface);
                    filter.addProperty("formFactor", formFactor);
                    filter.addProperty("capacity", capacity);
                    filter.add("products", gson.toJsonTree(capacitySet));
                    filters.add(filter);
                }
            }
        }

        //Create JSON data for all unique interfaces, form factors and capacities
        interfaces.keySet().remove("Any Interface");
        List<String> interfacesList = new ArrayList<>(interfaces.keySet());
        Collections.sort(interfacesList);
        interfacesList.add(0, "Any Interface");

        formFactors.keySet().remove("Any Form Factor");
        List<String> formFactorsList = new ArrayList<>(formFactors.keySet());
        Collections.sort(formFactorsList);
        formFactorsList.add(0, "Any Form Factor");

        List<CapacitySortable>capacitiesSortableList = new ArrayList<>(capacitiesSortable.values());
        capacitiesSortableList.sort(new CapacitySortable.SortCapacities());
        List<String> capacitiesList = new ArrayList<>();
        for (CapacitySortable capacitySortable : capacitiesSortableList) {
            capacitiesList.add(capacitySortable.getCapacity());
        }
        capacitiesList.add(0, "Any Capacity");

        JsonObject allFilters = new JsonObject();
        allFilters.add("interface", gson.toJsonTree(interfacesList));
        allFilters.add("formFactor", gson.toJsonTree(formFactorsList));
        allFilters.add("capacity", gson.toJsonTree(capacitiesList));

        //Write response to JSON
        JsonObject respJSON = new JsonObject();
        respJSON.add("allFilters", allFilters);
        respJSON.add("filters", filters);
        respJSON.add("products", products);
        resp.setContentType("application/json");
        resp.getWriter().write(respJSON.toString());
    }
}
