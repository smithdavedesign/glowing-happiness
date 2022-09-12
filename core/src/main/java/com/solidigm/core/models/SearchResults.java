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
     * Returns List of filters that were authored for the search results
     *
     * @return List<Filter>
     */
    default List<Filter> getFilters() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns List of Search Results
     *
     * @return string
     */
    default List<SearchResult> getSearchResults() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the number of results per page
     *
     * @return string
     */
    default String getResultsPerPage() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the total number of search results
     *
     * @return string
     */
    default String getTotalResults() {
        throw new UnsupportedOperationException();
    }

}

