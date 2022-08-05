package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;


/**
 * Defines the {@code Video} Sling Model for the {@code /apps/solidigm/components/video} component.
 */
public interface Video extends ComponentExporter {
    /**
     * Returns String for the path of Image Reference for the thumbnail to be displayed on the video
     *
     * @return String
     */
    default String getThumbnailImageReference() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the alt text of the video thumbnail image, if no text is authored then the alt text from the
     * DAM will be returned
     *
     * @return string
     */
    default String getThumbnailAltText() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns Boolean whether the component has content to display
     *
     * @return Boolean
     */
    default Boolean hasContent() {
        throw new UnsupportedOperationException();
    }
}
