package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.BioItem;

import java.util.List;

/**
 * Defines the {@code BioList} Sling Model for the {@code /apps/solidigm/components/biolist} component.
 */
public interface BioList extends ComponentExporter {
    /**
     * Returns List of BioItems to be displayed
     */
    default List<BioItem> getBioItems() { throw new UnsupportedOperationException(); }

    /**
     * Returns Boolean whether component has content to display
     */
    default Boolean hasContent() { throw new UnsupportedOperationException(); }
}
