package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.solidigm.core.beans.Image;
import com.solidigm.core.models.ProductPhotoGallery;
import com.solidigm.core.utils.ImageUtil;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.osgi.service.component.annotations.Reference;

import javax.annotation.PostConstruct;
import java.util.List;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {ProductPhotoGallery.class, ComponentExporter.class},
        resourceType = ProductPhotoGalleryImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class ProductPhotoGalleryImpl implements ProductPhotoGallery{
    static final String RESOURCE_TYPE = "solidigm/components/productphotogallery";

    @SlingObject
    SlingHttpServletRequest request;

    @ChildResource
    @Getter
    List<Image> images;

    @Getter
    private Image mainImage;

    @Override
    public Boolean hasContent() {
        return images != null && !images.isEmpty();
    }

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @PostConstruct
    private void initModel() {
        if(images != null) {
            for(Image image : images) {
                if(StringUtils.isEmpty(image.getAltText())) {
                    image.setAltText(ImageUtil.getAltTextFromDAM(request.getResourceResolver(), image.getImageReference()));
                }
            }
            mainImage = images.get(0);
        }
    }
}
