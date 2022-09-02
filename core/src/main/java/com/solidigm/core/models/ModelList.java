package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.Link;
import com.solidigm.core.beans.ProductPage;

import java.util.List;

/**
 * Defines the {@code ModelList} Sling Model for the {@code /apps/solidigm/components/modellist} component.
 */
public interface ModelList extends ComponentExporter {
    /**
     * Returns string for the title
     *
     * @return String
     */
    default String getTitle() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the current page path
     *
     * @return String
     */
    default String getCurrentPagePath() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns link to be displayed at the bottom of the component
     *
     * @return Link
     */
    default Link getLink() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns List of featured products
     *
     * @return List<ProductPage>
     */
    default List<ProductPage> getFeaturedProducts() {
        throw new UnsupportedOperationException();
    }

}

