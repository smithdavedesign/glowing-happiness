package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;

import java.util.List;

/**
 * Defines the {@code ProductOverview} Sling Model for the {@code /apps/solidigm/components/productoverview} component.
 */
public interface ProductOverview extends ComponentExporter {
    /**
     * Returns string for the product description
     *
     * @return String
     */
    default String getDescription() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the extra info about the product
     *
     * @return String
     */
    default String getExtraInfo() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string array for the product's interfaces
     *
     * @return List<String>
     */
    default List<String> getInterfaces() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string array for the product's Form Factors
     *
     * @return List<String>
     */
    default List<String> getFormFactors() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string array for the product's capacities
     *
     * @return List<String>
     */
    default List<String> getCapacities() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns link to the distributor page
     *
     * @return String
     */
    default String getDistributorPage() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns link to the product support page
     *
     * @return String
     */
    default String getSupportPage() {
        throw new UnsupportedOperationException();
    }

}

