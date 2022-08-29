package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.TileData;

import java.util.List;

/**
 * Defines the {@code TileList} Sling Model for the {@code /apps/solidigm/components/TileList} component.
 */
public interface TileList extends ComponentExporter {
    /**
     * Returns List of tiledata
     *
     * @return List<TileData>
     */
    default List<TileData> getTileInfo() {
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
