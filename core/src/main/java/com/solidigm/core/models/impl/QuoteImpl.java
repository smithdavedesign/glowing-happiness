package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.solidigm.core.models.Quote;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {Quote.class, ComponentExporter.class},
        resourceType = QuoteImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class QuoteImpl implements Quote {
    static final String RESOURCE_TYPE = "solidigm/components/quote";

    @ValueMapValue
    @Getter
    private String quote;

    @ValueMapValue
    @Getter
    private String name;

    @ValueMapValue
    @Getter
    private String title;

    private Boolean hasContentVal;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @Override
    public Boolean hasContent() {
        return hasContentVal;
    }

    @PostConstruct
    private void initModel() {
        hasContentVal = !StringUtils.isEmpty(quote) && !StringUtils.isEmpty(name);
    }
}
