package com.solidigm.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.solidigm.core.beans.Link;

import java.util.List;

/**
 * Defines the {@code Header} Sling Model for the {@code /apps/solidigm/components/header} component.
 */
public interface Header extends ComponentExporter {
    /**
     * Returns string for the Navigation Title 1 that will be displayed in the Top navbar
     *
     * @return string
     */
    default String getNavItemTitle1() {
        throw new UnsupportedOperationException();
    }


    /**
     * Returns string for the Navigation Title 2 that will be displayed in the Top navbar
     *
     * @return string
     */
    default String getNavItemTitle2() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the Navigation Title 3 that will be displayed in the Top navbar
     *
     * @return string
     */
    default String getNavItemTitle3() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the Navigation Title 1 that will be displayed in the Top navbar
     *
     * @return string
     */
    default String getNavItemTitle4() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the Navigation Title 5 that will be displayed in the Top navbar
     *
     * @return string
     */
    default String getNavItemTitle5() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the Navigation Title 1's link
     *
     * @return string
     */
    default String getNavItemLink1() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the Navigation Title 2's link
     *
     * @return string
     */
    default String getNavItemLink2() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the Navigation Title 3's link
     *
     * @return string
     */
    default String getNavItemLink3() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the Navigation Title 4's link
     *
     * @return string
     */
    default String getNavItemLink4() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the Navigation Title 5's link
     *
     * @return string
     */
    default String getNavItemLink5() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns list of title/links for the subnavigation items for Topnav item 1
     * If Topnav item 1 includes a link, subnav items for topnav item 1 will not show
     *
     * @return List<Link>
     */
    default List<Link> getSubNavItems1() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns list of title/links for the subnavigation items for Topnav item 2
     * If Topnav item 2 includes a link, subnav items for topnav item 2 will not show
     *
     * @return List<Link>
     */
    default List<Link> getSubNavItems2() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns list of title/links for the subnavigation items for Topnav item 3
     * If Topnav item 3 includes a link, subnav items for topnav item 3 will not show
     *
     * @return List<Link>
     */
    default List<Link> getSubNavItems3() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns list of title/links for the subnavigation items for Topnav item 4
     * If Topnav item 4 includes a link, subnav items for topnav item 4 will not show
     *
     * @return List<Link>
     */
    default List<Link> getSubNavItems4() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns list of title/links for the subnavigation items for Topnav item 5
     * If Topnav item 5 includes a link, subnav items for topnav item 5 will not show
     *
     * @return List<Link>
     */
    default List<Link> getSubNavItems5() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns list of title/link for language homepages
     *
     * @return List<Link>
     */
    default List<Link> getLangItems() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string of the greeting words to address the user if logged in
     *
     * @return string
     */
    default String getGreeting() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string for the searchbar placeholder text, the text that is shown until the user clicks the searchbar
     *
     * @return string
     */
    default String getPlaceholderText() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string of path to search results page
     *
     * @return string
     */
    default String getSearchResultsPage() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string of path to user dashboard
     *
     * @return string
     */
    default String getDashboard() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string of path to current language homepage
     *
     * @return string
     */
    default String getHomepage() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns string of HTML target value; changes based on which link is being processed
     *
     * @return string
     */
    default String getTarget() {
        throw new UnsupportedOperationException();
    }
}

