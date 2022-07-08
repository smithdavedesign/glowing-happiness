package com.solidigm.core.utils;

import com.drew.lang.annotations.NotNull;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

public class ImageUtil {
    /**
     * Returns the String of the Alt text stored in the DAM of the provided image
     *
     * @param resourceResolver JCR resource resolver
     * @param image Path of the image in the JCR to get the Alt Text of
     * @return String the alt text of the image found at the provided location in the JCR.
     * Empty string if no image is found or the alt text is not provided
     */
    public static String getAltTextFromDAM(@NotNull ResourceResolver resourceResolver, @NotNull String image) {
        Resource imageResource = resourceResolver.getResource(image.concat("/jcr:content/metadata"));
        if (imageResource != null) {
            return imageResource.getValueMap().get("dc:description", String.class);
        }
        return "";
    }
}
