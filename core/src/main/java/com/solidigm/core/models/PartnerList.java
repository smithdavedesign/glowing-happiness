package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.Partner;

import java.util.List;

/**
 * Defines the {@code PartnerList} Sling Model for the {@code /apps/solidigm/components/partnerlist} component.
 */
public interface PartnerList extends ComponentExporter {
    /**
     * Returns string for the column label of the listed partners
     *
     * @return string
     */
    default String getPartnerType() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns List of partners to be listed in the component
     *
     * @return List<Partner>
     */
    default List<Partner> getPartners() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns Boolean whether component has content to display
     *
     * @return Boolean
     */
    default Boolean hasContent() {
        throw new UnsupportedOperationException();
    }
}

