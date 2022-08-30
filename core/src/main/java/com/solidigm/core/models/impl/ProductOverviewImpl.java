package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.Page;
import com.solidigm.core.models.ProductOverview;
import com.solidigm.core.utils.SolidigmUtils;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

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

    @Getter
    private String[] interfaces;

    @Getter
    private String[] formFactors;

    @Getter
    private String[] capacities;

    @Getter
    private String distributorPage;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @PostConstruct
    private void initModel() {
        interfaces = new String[]{"Test: PCIE 4.0 X4, NVME"};
        formFactors = new String[]{"Test: U2 15mm", "E1.L 9.5mm", "E1.L 18mm"};
        capacities = new String[]{"Test: 1.6TB", "3.2 TB", "6.4TB"};

        if(currentPage != null) {
            distributorPage = SolidigmUtils.getSiteRoot(currentPage.getPath()).concat(DISTRIBUTOR_PAGE);
        }
    }
}
