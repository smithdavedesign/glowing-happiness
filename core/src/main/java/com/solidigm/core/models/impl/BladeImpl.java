package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.solidigm.core.beans.CallToActionButton;
import com.solidigm.core.beans.Link;
import com.solidigm.core.models.Blade;
import com.solidigm.core.utils.ImageUtil;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.List;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {Blade.class, ComponentExporter.class},
        resourceType = BladeImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class BladeImpl implements Blade {
    static final String RESOURCE_TYPE = "solidigm/components/blade";

    @SlingObject
    SlingHttpServletRequest request;

    @Getter
    @ValueMapValue
    private String title;

    @Getter
    @ValueMapValue
    private String description;

    @Getter
    @ValueMapValue
    private String imageReference;

    @Getter
    @ValueMapValue
    private String altText;

    @Getter
    @ChildResource(name = "bullets")
    private List<Link> bullets;

    @Getter
    @ChildResource(name = "callToActions")
    private List<CallToActionButton> callToActions;

    @ValueMapValue
    private String url;

    private Boolean hasContentValue;

    private Boolean showVideoValue;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @Override
    public Boolean showVideo() {
        return showVideoValue;
    }

    @Override
    public Boolean hasContent() {
        return hasContentValue;
    }

    @PostConstruct
    private void initModel() {
        hasContentValue = !StringUtils.isEmpty(imageReference);
        showVideoValue = !StringUtils.isEmpty(url);
        if (hasContentValue && StringUtils.isEmpty(altText)) {
            altText = ImageUtil.getAltTextFromDAM(request.getResourceResolver(), imageReference);
        }
    }
}
