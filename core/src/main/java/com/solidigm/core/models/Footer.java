package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.Link;

import java.util.List;

/**
 * Defines the {@code Footer} Sling Model for the {@code /apps/solidigm/components/Footer} component.
 */
public interface Footer extends ComponentExporter {
    /**
     * Returns string for the search title of the Footer
     *
     * @return string
     */
    default String getSearchTitle() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the placeholder search text of the Footer
     *
     * @return string
     */
    default String getPlaceholderText() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the path to the Search Results Page
     *
     * @return string
     */
    default String getSearchResultsPage() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns List of links for searching by tags
     *
     * @return List<Link>
     */
    default List<Link> getSearchTagsLinks() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the disclaimer of the Footer with html tags
     *
     * @return string
     */
    default String getDisclaimer() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the trademark text of the Footer with html tags
     *
     * @return String
     */
    default String getTrademark() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the copyright text of the Footer with html tags
     *
     * @return string
     */
    default String getCopyright() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns Link for the Home Page link of the Footer
     *
     * @return Link
     */
    default Link getHomePage() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns Link for the facebook link of the Footer
     *
     * @return Link
     */
    default Link getFacebook() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns Link for the LinkedIn link of the footer
     *
     * @return Link
     */
    default Link getLinkedIn() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns Link for the Twitter link of the footer
     *
     * @return Link
     */
    default Link getTwitter() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns Link for the YouTube link of the footer
     *
     * @return Link
     */
    default Link getYouTube() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns Link for the TrustArc link of the Footer
     *
     * @return Link
     */
    default Link getTrustArc() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns List of external links
     *
     * @return List<callToActionButton>
     */
    default List<Link> getLinks() {
        throw new UnsupportedOperationException();
    }    

    /**
     * Returns List of Tag labels to display
     *
     * @return List<callToActionButton>
     */
    default List<String> getTagLabels() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns Boolean whether the footer has content to display
     *
     * @return Boolean
     */
    default Boolean hasContent() {
        throw new UnsupportedOperationException();
    }
}
