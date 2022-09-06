package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.Filter;
import com.solidigm.core.beans.SearchResult;

import java.util.List;

/**
 * Defines the {@code SearchResults} Sling Model for the {@code /apps/solidigm/components/searchresults} component.
 */
public interface SearchResults extends ComponentExporter {
    /**
     * Returns string for the Quoted text; includes any authored line end characters
     *
     * @return string
     */
    default List<Filter> getFilters() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the Quoted text; includes any authored line end characters
     *
     * @return string
     */
    default List<SearchResult> getSearchResults() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the Quoted text; includes any authored line end characters
     *
     * @return string
     */
    default String getResultsPerPage() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the Quoted text; includes any authored line end characters
     *
     * @return string
     */
    default String getTotalResults() {
        throw new UnsupportedOperationException();
    }

}

