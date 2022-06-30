package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.QuickLinkTile;

import java.util.List;

/**
 * Defines the {@code QuickLinks} Sling Model for the {@code /apps/solidigm/components/quicklinks} component.
 */
public interface QuickLinks extends ComponentExporter {
    /**
     * Returns string for the title to be displayed before all the tiles
     *It is optional
     *
     * @return string
     */
    public String getTitle();

    /**
     * Returns string for the label of the 'Read More' link
     *
     * @return string
     */
    public String getLinkLabel();

    /**
     * Returns string for the "Read More" link
     *
     * @return string
     */
    public String getLink();

    /**
     * Returns all of the tiles & their respective title, subtitle, link, and target
     *
     * @return List<QuickLinkTile>
     */
    public List<QuickLinkTile> getTileInfo();

    /**
     * Returns string of which frame to open link in
     * Target gets updated each time a link is processed
     *
     * @return String
     */
    public String getTarget();

    /**
     * Returns true (component has content) if a title or at least 1 tile has been authored
     *
     * @return Boolean
     */
    public Boolean hasContent();

}
