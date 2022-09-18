package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.Page;
import com.solidigm.core.beans.ProductPage;
import com.solidigm.core.models.SimilarProducts;
import com.solidigm.core.services.ProductService;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {SimilarProducts.class, ComponentExporter.class},
        resourceType = SimilarProductsImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class SimilarProductsImpl implements SimilarProducts {
    static final String RESOURCE_TYPE = "solidigm/components/similarproducts";

    @SlingObject
    SlingHttpServletRequest request;

    @OSGiService
    ProductService productService;

    @Inject
    @Source("script-bindings")
    Page currentPage;

    @ChildResource
    @Getter
    private List<ProductPage> similarProducts;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @PostConstruct
    private void initModel() {
        if(currentPage != null && similarProducts != null) {
            similarProducts.add(0, new ProductPage(currentPage.getPath(), request.getResourceResolver(), productService));
        }
    }
}
