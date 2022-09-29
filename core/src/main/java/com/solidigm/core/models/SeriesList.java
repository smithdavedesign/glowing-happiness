package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.ProductSeriesPage;

import java.util.List;

/**
 * Defines the {@code Quote} Sling Model for the {@code /apps/solidigm/components/quote} component.
 */
public interface SeriesList extends ComponentExporter {
    /**
     * Returns List of ProductSeriesPages
     *
     * @return List<ProductSeriesPage>
     */
    default List<ProductSeriesPage> getSeriesPages() {
        throw new UnsupportedOperationException();
    }
}

