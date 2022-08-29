package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.Image;

import java.util.List;

/**
 * Defines the {@code ProductPhotoGallery} Sling Model for the {@code /apps/solidigm/components/productphotogallery} component.
 */
public interface ProductPhotoGallery extends ComponentExporter {
    /**
     * Returns List of Images to be displayed in the product photo gallery
     *
     * @return List<Image>
     */
    default List<Image> getImages() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the first image in the product gallery
     *
     * @return Image
     */
    default Image getMainImage() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns Boolean whether component has content to display
     *
     * @return Boolean
     */
    default Boolean hasContent() {
        throw new UnsupportedOperationException();
    }
}

