package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.Page;
import com.solidigm.core.beans.Link;
import com.solidigm.core.beans.ProductPage;
import com.solidigm.core.models.ModelList;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {ModelList.class, ComponentExporter.class},
        resourceType = ModelListImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class ModelListImpl implements ModelList {
    static final String RESOURCE_TYPE = "solidigm/components/modellist";

    @Inject
    @Source("script-bindings")
    Page currentPage;

    @ValueMapValue(name = "label")
    private String linkLabel;

    @ValueMapValue(name = "link")
    private String linkTarget;

    @ValueMapValue
    @Getter
    private String title;

    @ChildResource
    @Getter
    private List<ProductPage> featuredProducts;

    @Getter
    private String currentPagePath;

    @Getter
    private Link link;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @PostConstruct
    private void initModel() {
        if(currentPage != null) {
           currentPagePath = currentPage.getPath();
        }

        if(StringUtils.isNotEmpty(linkLabel) && StringUtils.isNotEmpty(linkTarget)) {
            link = new Link(linkLabel, linkTarget);
        }
    }
}
