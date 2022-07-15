package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;

/**
 * Defines the {@code SocialShare} Sling Model for the {@code /apps/solidigm/components/socialshare} component.
 */
public interface SocialShare extends ComponentExporter {


    /**
     * Returns the link to the next article
     */
    default String getNext() { throw new UnsupportedOperationException(); }

    /**
     * Returns the link to the previous article
     */
    default String getPrevious() { throw new UnsupportedOperationException(); }
}
