package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.ProductPage;

import java.util.List;

/**
 * Defines the {@code SimilarProducts} Sling Model for the {@code /apps/solidigm/components/similarproducts} component.
 */
public interface SimilarProducts extends ComponentExporter {
    /**
     * Returns string for the product description
     *
     * @return String
     */
    default List<ProductPage> getSimilarProducts() {
        throw new UnsupportedOperationException();
    }
}

