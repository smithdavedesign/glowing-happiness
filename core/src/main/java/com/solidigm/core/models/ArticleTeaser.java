package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.ArticlePage;

/**
 * Defines the {@code ArticleTeaser} Sling Model for the {@code /apps/solidigm/components/articleteaser} component.
 */
public interface ArticleTeaser extends ComponentExporter {

    /**
     * Returns Article Pages data structure with teaser data to be displayed
     *
     * @return ArticlePage
     */
    default ArticlePage getArticle() {
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
