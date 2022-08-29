package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.solidigm.core.beans.Attribute;
import com.solidigm.core.beans.Highlight;
import com.solidigm.core.beans.Link;
import com.solidigm.core.beans.PcnMddsEntry;
import com.solidigm.core.models.ProductConfigurator;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
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

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @PostConstruct
    private void initModel() {
        downloadLink = new Link("", download);
        if (attributes != null) {
            for (Attribute attribute : attributes) {
                if (StringUtils.isEmpty(attribute.getAttributeValue())) {
                    attribute.setAttributeValue("Placeholder Value from Product Data");
                }
            }
        }
    }
}
