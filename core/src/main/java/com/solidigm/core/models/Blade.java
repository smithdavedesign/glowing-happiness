package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.CallToActionButton;
import com.solidigm.core.beans.Link;

import java.util.List;


/**
 * Defines the {@code Blade} Sling Model for the {@code /apps/solidigm/components/blade} component.
 */
public interface Blade extends ComponentExporter {
    /**
     * Returns String for the title to be displayed on the blade
     *
     * @return String
     */
    default String getTitle() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns String for the description to be displayed on the blade
     *
     * @return String
     */
    default String getDescription() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns String for the path of Image Reference for the image to be displayed on the blade
     *
     * @return String
     */
    default String getImageReference() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the alt text of the blade image, if no text is authored then the alt text from the DAM will
     * be returned
     *
     * @return string
     */
    default String getAltText() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns List of Links for the bullet points to be displayed on the blade
     *
     * @return List<Link>
     */
    default List<Link> getBullets() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns List of CallToActionButtons to be displayed on the blade
     *
     * @return List<CallToActionButton>
     */
    default List<CallToActionButton> getCallToActions() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns Boolean whether the blade is to display a video
     *
     * @return Boolean
     */
    default Boolean showVideo() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns Boolean whether the blade has content to display
     *
     * @return Boolean
     */
    default Boolean hasContent() {
        throw new UnsupportedOperationException();
    }
}
