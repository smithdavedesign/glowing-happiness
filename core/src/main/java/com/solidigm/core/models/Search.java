package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.Param;

import java.util.List;

/**
 * Defines the {@code Search} Sling Model for the {@code /apps/solidigm/components/search} component.
 */
public interface Search extends ComponentExporter {
    /**
     * Returns string for the search title
     *
     * @return string
     */
    default String getSearchTitle() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the placeholder text in the search bar
     *
     * @return string
     */
    default String getPlaceholderText() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the path to the search results page
     *
     * @return string
     */
    default String getSearchResultsPage() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns List of Params that were authored for the search component
     *
     * @return List<Param>
     */
    default List<Param> getParams() {
        throw new UnsupportedOperationException();
    }
}

