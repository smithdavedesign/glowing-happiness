package com.solidigm.core.beans;

import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.solidigm.core.utils.ImageUtil;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

public class SearchResultItem {
    @Getter
    private String pagePath;

    @Getter
    private String title;

    @Getter
    private String description;

    @Getter
    private String url;

    @Getter
    private Tag[] tags;

    @Getter
    private String image;

    @Getter
    private String altText;

    public SearchResultItem(String pagePath, ResourceResolver resourceResolver) {
        this.pagePath = pagePath;
        if (pagePath != null) {
            Resource resultResource = resourceResolver.getResource(pagePath);
            Page resultPage = resultResource.adaptTo(Page.class);
            title = resultPage.getTitle();
            description = resultPage.getDescription();
            url = new Link("", pagePath).getLink();
            tags = resultPage.getTags();

            //Set Teaser image and alt text
            Resource productImage = resultResource.getChild("jcr:content").getChild("cq:featuredimage");
            if (productImage != null) {
                image = productImage.getValueMap().get("fileReference", String.class);
                if (Boolean.TRUE.equals(productImage.getValueMap().get("altValueFromDAM", Boolean.class)) && image != null) {
                    altText = ImageUtil.getAltTextFromDAM(resourceResolver, image);
                } else {
                    altText = productImage.getValueMap().get("alt", String.class);
                }
            }
        }
    }
}
