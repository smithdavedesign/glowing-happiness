package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.solidigm.core.beans.QuickLink;
import com.solidigm.core.models.QuickLinks;
import com.solidigm.core.utils.LinkUtil;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {QuickLinks.class, ComponentExporter.class},
        resourceType = QuickLinksImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class QuickLinksImpl implements QuickLinks {
    static final String RESOURCE_TYPE = "solidigm/components/quicklinks";

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @Getter
    @ValueMapValue
    private String title;

    @Getter
    @ValueMapValue
    private String linkLabel;

    @ValueMapValue
    private String link;

    @Getter
    @ChildResource(name = "linkInfo")
    private List<QuickLink> linkInfo;

    @Getter
    private String target;

    public String getLink() {
        link = LinkUtil.processLink(link);
        target = LinkUtil.processTarget(link);
        return link;
    }

    @Override
    public Boolean hasContent() {
        if ((title == null) && (linkInfo == null)) {
            return false;
        }
        return true;
    }
}
