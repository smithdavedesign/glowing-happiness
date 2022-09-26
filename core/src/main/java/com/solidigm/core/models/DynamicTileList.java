package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;

/**
 * Defines the {@code DynamicTileList} Sling Model for the {@code /apps/solidigm/components/dynamictilelist} component.
 */
public interface DynamicTileList extends ComponentExporter {

    /**
     * Returns the path to the dynamic tile list component
     *
     * @return String
     */
    default String getPath() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns Boolean whether the tileList has content to display
     *
     * @return Boolean
     */
    default Boolean hasContent() {
        throw new UnsupportedOperationException();
    }
}
