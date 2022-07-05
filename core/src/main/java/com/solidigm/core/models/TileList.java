package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.Link;
import com.solidigm.core.beans.TileData;

import java.util.List;

/**
 * Defines the {@code Footer} Sling Model for the {@code /apps/solidigm/components/Footer} component.
 */
public interface TileList extends ComponentExporter {
    /**
     * Returns List of links for searching by tags
     *
     * @return List<TileData>
     */
    default List<TileData> tileInfo() {
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
