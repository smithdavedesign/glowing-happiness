package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;

/**
 * Defines the {@code Quote} Sling Model for the {@code /apps/solidigm/components/quote} component.
 */
public interface Quote extends ComponentExporter {
    /**
     * Returns string for the Quoted text; includes any authored line end characters
     *
     * @return string
     */
    default String getQuote() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the Author's name attributed to the quote
     *
     * @return string
     */
    default String getName() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the Author's title attributed to the quote
     *
     * @return string
     */
    default String getTitle() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns Boolean whether component has content to display
     *
     * @return Boolean
     */
    default Boolean hasContent() {
        throw new UnsupportedOperationException();
    }
}

