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
    public String getNavItemTitle1();

    /**
     * Returns string for the Navigation Title 2 that will be displayed in the Top navbar
     *
     * @return string
     */
    public String getNavItemTitle2();

    /**
     * Returns string for the Navigation Title 3 that will be displayed in the Top navbar
     *
     * @return string
     */
    public String getNavItemTitle3();

    /**
     * Returns string for the Navigation Title 1 that will be displayed in the Top navbar
     *
     * @return string
     */
    public String getNavItemTitle4();

    /**
     * Returns string for the Navigation Title 5 that will be displayed in the Top navbar
     *
     * @return string
     */
    public String getNavItemTitle5();

    /**
     * Returns string for the Navigation Title 1's link
     *
     * @return string
     */
    public String getNavItemLink1();

    /**
     * Returns string for the Navigation Title 2's link
     *
     * @return string
     */
    public String getNavItemLink2();

    /**
     * Returns string for the Navigation Title 3's link
     *
     * @return string
     */
    public String getNavItemLink3();

    /**
     * Returns string for the Navigation Title 4's link
     *
     * @return string
     */
    public String getNavItemLink4();

    /**
     * Returns string for the Navigation Title 5's link
     *
     * @return string
     */
    public String getNavItemLink5();

    /**
     * Returns list of title/links for the subnavigation items for Topnav item 1
     * If Topnav item 1 includes a link, subnav items for topnav item 1 will not show
     *
     * @return List<Link>
     */
    public List<Link> getSubNavItems1();

    /**
     * Returns list of title/links for the subnavigation items for Topnav item 2
     * If Topnav item 2 includes a link, subnav items for topnav item 2 will not show
     *
     * @return List<Link>
     */
    public List<Link> getSubNavItems2();

    /**
     * Returns list of title/links for the subnavigation items for Topnav item 3
     * If Topnav item 3 includes a link, subnav items for topnav item 3 will not show
     *
     * @return List<Link>
     */
    public List<Link> getSubNavItems3();

    /**
     * Returns list of title/links for the subnavigation items for Topnav item 4
     * If Topnav item 4 includes a link, subnav items for topnav item 4 will not show
     *
     * @return List<Link>
     */
    public List<Link> getSubNavItems4();

    /**
     * Returns list of title/links for the subnavigation items for Topnav item 5
     * If Topnav item 5 includes a link, subnav items for topnav item 5 will not show
     *
     * @return List<Link>
     */
    public List<Link> getSubNavItems5();

    /**
     * Returns list of title/link for language homepages
     *
     * @return List<Link>
     */
    public List<Link> getLangItems();

    /**
     * Returns string of the greeting words to address the user if logged in
     *
     * @return string
     */
    public String getGreeting();

    /**
     * Returns string for the searchbar placeholder text, the text that is shown until the user clicks the searchbar
     *
     * @return string
     */
    public String getPlaceholderText();

    /**
     * Returns string of path to search results page
     *
     * @return string
     */
    public String getSearchResultsPage();

    /**
     * Returns string of path to user dashboard
     *
     * @return string
     */
    public String getDashboard();

    /**
     * Returns string of path to current language homepage
     *
     * @return string
     */
    public String getHomepage();

    /**
     * Returns string of HTML target value; changes based on which link is being processed
     *
     * @return string
     */
    public String getTarget();
}

