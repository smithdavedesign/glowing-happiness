package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.solidigm.core.beans.BioItem;
import com.solidigm.core.models.BioList;
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

import javax.annotation.PostConstruct;
import java.util.List;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {BioList.class, ComponentExporter.class},
        resourceType = BioListImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class BioListImpl implements BioList {
    static final String RESOURCE_TYPE = "solidigm/components/biolist";

    @SlingObject
    SlingHttpServletRequest request;

    @ChildResource(name = "bios")
    @Getter
    private List<BioItem> bioItems;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @Override
    public Boolean hasContent() {
        return (bioItems != null && !bioItems.isEmpty());
    }

    @PostConstruct
    private void initModel() {
        if(bioItems != null) {
            for (BioItem bio : bioItems) {
                if (StringUtils.isEmpty(bio.getImageAltText()) && !StringUtils.isEmpty(bio.getImageReference())) {
                    bio.setImageAltText(ImageUtil.getAltTextFromDAM(request.getResourceResolver(), bio.getImageReference()));
                }
            }
        }
    }
}
