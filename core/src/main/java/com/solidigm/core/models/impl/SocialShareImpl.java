package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.solidigm.core.models.Footer;
import com.solidigm.core.models.SocialShare;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {SocialShare.class, ComponentExporter.class},
        resourceType = SocialShareImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class SocialShareImpl implements SocialShare {

    static final String RESOURCE_TYPE = "solidigm/components/socialshare";

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
}
