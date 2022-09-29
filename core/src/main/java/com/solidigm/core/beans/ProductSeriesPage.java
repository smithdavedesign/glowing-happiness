package com.solidigm.core.beans;

import com.day.cq.wcm.api.Page;
import com.solidigm.core.utils.ImageUtil;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

public class ProductSeriesPage {
    @Getter
    private String seriesPagePath;

    @Getter
    private Link seriesPageLink;

    @Getter
    private String name;

    @Getter
    private String description;

    @Getter
    private String image;

    @Getter
    private String altText;

    public ProductSeriesPage(ResourceResolver resourceResolver, String seriesPagePath) {
        this.seriesPagePath = seriesPagePath;
        this.seriesPageLink = new Link("", seriesPagePath);
        if (seriesPagePath != null) {
            Resource seriesPageResource = resourceResolver.getResource(seriesPagePath);
            if (seriesPageResource != null) {
                //Set teaser information from the page properties
                Page seriesPage = seriesPageResource.adaptTo(Page.class);
                name = seriesPage.getTitle();
                description = seriesPage.getDescription();

                //Set Teaser image and alt text
                Resource seriesImage = seriesPageResource.getChild("jcr:content").getChild("cq:featuredimage");
                if (seriesImage != null) {
                    image = seriesImage.getValueMap().get("fileReference", String.class);
                    if (Boolean.TRUE.equals(seriesImage.getValueMap().get("altValueFromDAM", Boolean.class))) {
                        altText = ImageUtil.getAltTextFromDAM(resourceResolver, image);
                    } else {
                        altText = seriesImage.getValueMap().get("alt", String.class);
                    }
                }
            }
        }
    }
}
