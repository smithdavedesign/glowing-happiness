package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.Filter;

import java.util.List;

/**
 * Defines the {@code SearchResults} Sling Model for the {@code /apps/solidigm/components/searchresults} component.
 */
public interface SearchResults extends ComponentExporter {
    /**
     * Returns List of filters that were authored for the search results
     *
     * @return List<Filter>
     */
    default List<Filter> getFilters() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the number of results per page
     *
     * @return string
     */
    default String getRootPath() {
        throw new UnsupportedOperationException();
    }
}

