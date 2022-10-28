package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.solidigm.core.models.Video;
import com.solidigm.core.services.BrightcoveVideoService;
import com.solidigm.core.utils.ImageUtil;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {Video.class, ComponentExporter.class},
        resourceType = VideoImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class VideoImpl implements Video {
    static final String RESOURCE_TYPE = "solidigm/components/video";

    @SlingObject
    ResourceResolver resourceResolver;

    @OSGiService
    BrightcoveVideoService service;

    @ValueMapValue
    @Getter
    private String thumbnailImageReference;

    @ValueMapValue
    @Getter
    private String thumbnailAltText;

    @Getter
    private String accountId;

    @Getter
    private String playerId;

    @ValueMapValue
    private String url;

    @Getter
    @ValueMapValue
    private String brightcoveId;

    @Override
    public Boolean hasContent() {
        return  StringUtils.isNotEmpty(url) || StringUtils.isNotEmpty(brightcoveId);
    }

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @PostConstruct
    private void initModel() {
        playerId = service.getPlayerId();
        accountId = service.getAccountId();
        if(StringUtils.isEmpty(thumbnailAltText) && StringUtils.isNotEmpty(thumbnailImageReference)) {
            thumbnailAltText = ImageUtil.getAltTextFromDAM(resourceResolver, thumbnailImageReference);
        }
    }

    public boolean isBrightcovePresent(){
        return !StringUtils.isEmpty(brightcoveId);
    }
}