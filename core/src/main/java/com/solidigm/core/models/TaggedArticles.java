package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.day.cq.tagging.Tag;
import com.solidigm.core.beans.ArticlePage;

import java.util.List;

/**
 * Defines the {@code TaggedArticles} Sling Model for the {@code /apps/solidigm/components/taggedarticles} component.
 */
public interface TaggedArticles extends ComponentExporter {

    /**
     * Returns the root path for the localized site
     *
     * @return String
     */
    default String getRootPath() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the queried tag in the selector if it exists
     *
     * @return String
     */
    default String getSelectorTag() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns List of Tags to populate the dropdown list for filtering Tagged Articles
     *
     * @return List<Tag>
     */
    default List<Tag> getDropdownTags() {
        throw new UnsupportedOperationException();
    }
}
