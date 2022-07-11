package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.wcm.core.components.models.NavigationItem;
import com.solidigm.core.beans.AnchorLink;
import com.solidigm.core.beans.CallToActionButton;

import java.util.List;

/**
 * Defines the {@code Hero} Sling Model for the {@code /apps/solidigm/components/hero} component.
 */
public interface Hero extends ComponentExporter {
    /**
     * Returns string for the title to be displayed on the hero
     *
     * @return string
     */
    default String getTitle() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the subtitle to be displayed on the hero
     *
     * @return string
     */
    default String getSubtitle() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the description to be displayed on the hero as rich text
     *
     * @return string
     */
    default String getDescription() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns boolean for whether to show the breadcrumb based on the breadcrumb selection
     *
     * @return Boolean
     */
    default Boolean getShowBreadcrumb() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the path to the desktop image reference
     *
     * @return string
     */
    default String getDesktopImageReference() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the alt text of the Desktop image, if no text is authored then the alt text from the DAM will
     * be returned
     *
     * @return string
     */
    default String getDesktopAltText() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the path to the mobile image reference
     *
     * @return string
     */
    default String getMobileImageReference() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the alt text of the Mobile image, if no text is authored then the alt text from the DAM will
     * be returned
     *
     * @return string
     */
    default String getMobileAltText() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the filter selection
     *
     * @return string
     */
    default String getFilter() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns List of Call-to-Action Buttons
     *
     * @return List<callToActionButton>
     */
    default List<CallToActionButton> getCallToActions() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns List of anchor links
     *
     * @return List<anchorLink>
     */
    default List<AnchorLink> getAnchors() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns List of Navigation Items for the breadcrumb, always a max of two
     *
     * @return List<NavigationItem>
     */
    default List<NavigationItem> getBreadcrumbItems() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns Boolean whether hero has content to display
     *
     * @return Boolean
     */
    default Boolean hasContent() {
        throw new UnsupportedOperationException();
    }
}

