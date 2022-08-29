package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.Link;

import java.util.List;

/**
 * Defines the {@code EventInfo} Sling Model for the {@code /apps/solidigm/components/eventinfo} component.
 */
public interface EventInfo extends ComponentExporter {
    /**
     * Returns string for the date the article was posted, formatted to be used for SEO
     *
     * @return string
     */
    default String getDate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the date the article was posted, formatted to be displayed to the user
     *
     * @return string
     */
    default String getPostedDate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the starting date of the event
     *
     * @return string
     */
    default String getStartDate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for ending date of the event
     *
     * @return string
     */
    default String getEndDate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the starting time of the event
     *
     * @return string
     */
    default String getStartTime() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the ending time of the event
     *
     * @return string
     */
    default String getEndTime() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the location for the event
     *
     * @return string
     */
    default String getLocation() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for additional event info provided by the author
     *
     * @return string
     */
    default String getEventInfo() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns List of Links for the tags of the article, links to tagged articles page querying selected tag
     *
     * @return List<Link>
     */
    default List<Link> getTags() {
        throw new UnsupportedOperationException();
    }
}

