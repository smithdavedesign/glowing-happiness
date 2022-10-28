package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.Page;
import com.solidigm.core.beans.ProductModel;
import com.solidigm.core.models.ProductOverview;
import com.solidigm.core.services.ProductService;
import com.solidigm.core.services.SocialService;
import com.solidigm.core.utils.SolidigmUtils;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static com.solidigm.core.utils.SolidigmConstants.HOME_NAVIGATION_LEVEL;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {ProductOverview.class, ComponentExporter.class},
        resourceType = ProductOverviewImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class ProductOverviewImpl implements ProductOverview {
    static final String RESOURCE_TYPE = "solidigm/components/productoverview";
    static final String DISTRIBUTOR_PAGE = "/authorized-distributors";

    @OSGiService
    ProductService productService;

    @Inject
    @Source("script-bindings")
    Page currentPage;

    @ValueMapValue
    @Getter
    private String description;

    @ValueMapValue
    @Getter
    private String extraInfo;

    @ValueMapValue
    @Getter
    private String supportPage;

    @ValueMapValue
    @Getter
    private String distributorPage;

    @Getter
    private List<String> interfaces;

    @Getter
    private List<String> formFactors;

    @Getter
    private List<String> capacities;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @PostConstruct
    private void initModel() {

        //Retrieve product info from the Product Service
        String pagePath = currentPage.getPath();
        ProductModel product = productService.getProduct(SolidigmUtils.getProductCategoryFromPath(pagePath),
                                                         SolidigmUtils.getProductSeriesFromPath(pagePath),
                                                         SolidigmUtils.getProductNameFromPath(pagePath));
        interfaces = product.getInterfaces();
        formFactors = product.getFormFactors();
        capacities = product.getCapacities();

        if(currentPage != null && distributorPage == null) {
            distributorPage = SolidigmUtils.getSiteRoot(currentPage.getPath()).concat(DISTRIBUTOR_PAGE);
        }
    }
}
