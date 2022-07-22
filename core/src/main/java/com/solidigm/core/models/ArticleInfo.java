package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.Link;

import java.util.List;

/**
 * Defines the {@code ArticleInfo} Sling Model for the {@code /apps/solidigm/components/articleinfo} component.
 */
public interface ArticleInfo extends ComponentExporter {
    /**
     * Returns string for the date the article was posted
     *
     * @return string
     */
    default String getDate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the Author's name
     *
     * @return string
     */
    default String getAuthor() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns List of Links for the tags of the article, always starting with the category tag,
     * links to tagged articles page querying selected tag
     *
     * @return List<Link>
     */
    default List<Link> getTags() {
        throw new UnsupportedOperationException();
    }
}

