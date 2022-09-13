package com.solidigm.core.services;

import com.solidigm.core.beans.ProductModel;

public interface ProductService {
    /**
     * Returns the Product Model associated with category, series, and name provided
     * @param category - name of the product category
     * @param series - name of the product series
     * @param name - name of the product
     * @return ProductModel
     */
    default ProductModel getProduct(String category, String series, String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * Empties the cache of product data so that new data from the JCR can be loaded.
     */
    default void reset() {
        throw new UnsupportedOperationException();
    }
}
