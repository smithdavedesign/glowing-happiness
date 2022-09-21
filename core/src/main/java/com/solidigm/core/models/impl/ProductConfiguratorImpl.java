package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.Page;
import com.solidigm.core.beans.*;
import com.solidigm.core.models.ProductConfigurator;
import com.solidigm.core.services.ProductService;
import com.solidigm.core.utils.SolidigmUtils;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {ProductConfigurator.class, ComponentExporter.class},
        resourceType = ProductConfiguratorImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class ProductConfiguratorImpl implements ProductConfigurator {
    static final String RESOURCE_TYPE = "solidigm/components/productconfigurator";

    @OSGiService
    ProductService productService;

    @Inject
    @Source("script-bindings")
    Page currentPage;

    @ChildResource
    @Getter
    private List<Attribute> attributes;

    @ChildResource
    @Getter
    private List<Highlight> highlights;

    @ChildResource(name = "items")
    @Getter
    private List<PcnMddsEntry> pcnMddsEntries;

    @ValueMapValue
    private String download;

    @Getter
    private Link downloadLink;

    @Getter
    private String path;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @PostConstruct
    private void initModel() {
        downloadLink = new Link("", download);
        if (currentPage != null) {
            path = currentPage.getPath();
            ProductModel productData = productService.getProduct(SolidigmUtils.getProductCategoryFromPath(path),
                                                                 SolidigmUtils.getProductSeriesFromPath(path),
                                                                 SolidigmUtils.getProductNameFromPath(path));

            // Set
            if (attributes != null) {
                for (Attribute attribute : attributes) {
                    if (StringUtils.isEmpty(attribute.getAttributeValue()) &&
                            (attribute.getAttribute().equals("ProductGroup"))) {
                        attribute.setAttributeValue(productData.getProductGroup());
                    }
                }
            }
        }
    }
}
