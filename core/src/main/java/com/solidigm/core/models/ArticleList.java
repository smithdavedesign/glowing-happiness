package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.ArticlePage;

import java.util.List;

/**
 * Defines the {@code ArticleList} Sling Model for the {@code /apps/solidigm/components/articlelist} component.
 */
public interface ArticleList extends ComponentExporter {
    /**
     * Returns string for the title of the Article List
     *
     * @return string
     */
    default String getTitle() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the text of the link out to hub page for the Article List
     *
     * @return string
     */
    default String getLinkText() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the path for the link to the hub page for the Article List
     *
     * @return string
     */
    default String getLink() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns List of Article Pages to be displayed in the Article List
     *
     * @return List<ArticlePage>
     */
    default List<ArticlePage> getArticles() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns Boolean whether the Article List has content to display
     *
     * @return Boolean
     */
    default Boolean hasContent() {
        throw new UnsupportedOperationException();
    }
}
