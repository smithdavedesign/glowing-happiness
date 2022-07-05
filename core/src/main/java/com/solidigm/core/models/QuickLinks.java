package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.QuickLink;

import java.util.List;

/**
 * Defines the {@code QuickLinks} Sling Model for the {@code /apps/solidigm/components/quicklinks} component.
 */
public interface QuickLinks extends ComponentExporter {
    /**
     * Returns string for the title to be displayed before all the links
     *It is optional
     *
     * @return string
     */
    default String getTitle() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the label of the 'Read More' link
     *
     * @return string
     */
    default String getLinkLabel() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the "Read More" link
     *
     * @return string
     */
    default String getLink() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns all of the links & their respective title, subtitle, link, and target
     *
     * @return List<QuickLink>
     */
    default List<QuickLink> getLinkInfo() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string of which frame to open link in
     * Target gets updated each time a link is processed
     *
     * @return String
     */
    default String getTarget() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns true (component has content) if a title or at least 1 link has been authored
     *
     * @return Boolean
     */
    default Boolean hasContent() {
        throw new UnsupportedOperationException();
    }

}
