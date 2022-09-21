package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.Attribute;
import com.solidigm.core.beans.Highlight;
import com.solidigm.core.beans.Link;
import com.solidigm.core.beans.PcnMddsEntry;

import java.util.List;

/**
 * Defines the {@code ProductConfigurator} Sling Model for the {@code /apps/solidigm/components/productconfigurator} component.
 */
public interface ProductConfigurator extends ComponentExporter {
    /**
     * Returns the product page path of the current page
     *
     * @return String
     */
    default String getPath() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns list of attributes to display about the product
     *
     * @return List<Attribute>
     */
    default List<Attribute> getAttributes() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns List of highlights to display about the product
     *
     * @return List<Highlight>
     */
    default List<Highlight> getHighlights() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns list of pcn and mdds entries for the product
     *
     * @return List<PcnMddsEntry>
     */
    default List<PcnMddsEntry> getPcnMddsEntries() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns Link to download resource
     *
     * @return Link
     */
    default Link getDownloadLink() {
        throw new UnsupportedOperationException();
    }

}

