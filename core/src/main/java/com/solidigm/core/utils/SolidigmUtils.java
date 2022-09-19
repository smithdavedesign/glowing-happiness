package com.solidigm.core.utils;

import org.apache.commons.lang.StringUtils;

import static com.solidigm.core.utils.SolidigmConstants.HOME_NAVIGATION_LEVEL;

public class SolidigmUtils {

    /**
     * Finds the path of the site root of the provided page. E.g. /content/solidigm/us/en
     *
     * @param pagePath the path of a content page
     * @return String the path of the site root
     */
    public static String getSiteRoot(String pagePath) {
        int endIndex = StringUtils.ordinalIndexOf(pagePath, "/", HOME_NAVIGATION_LEVEL);
        if(endIndex != -1) {
            return pagePath.substring(0, endIndex);
        } else {
            return pagePath;
        }
    }

    /**
     * Returns the product category node name found in the provided page path.
     * Assumes a product category page is 2 levels above the page corresponding to the provided page path,
     * which should be the case for a PDP.
     * TODO: Check that the provided page path is for a PDP based on the template type.
     *
     * @param pagePath the path of a PDP (product model) page
     * @return String the node name of the product category page above the page corresponding to the provided path
     */
    public static String getProductCategoryFromPath(String pagePath) {
        String[] path = pagePath.split("/");
        return path[path.length - 3];
    }

    /**
     * Returns the product series node name found in the provided page path.
     * Assumes a product series page is 1 level above the page corresponding to the provided page path,
     * which should be the case for a PDP.
     * TODO: Check that the provided page path is for a PDP based on the template type.
     *
     * @param pagePath the path of a PDP (product model) page
     * @return String the node name of the product series page above the page corresponding to the provided path
     */
    public static String getProductSeriesFromPath(String pagePath) {
        String[] path = pagePath.split("/");
        return path[path.length - 2];
    }

    /**
     * Returns the product model node name found in the provided page path
     * Assumes the provided page path corresponds to a product model page (PDP).
     * TODO: Check that the provided page path is for a PDP based on the template type.
     *
     * @param pagePath the path of a PDP (product model) page
     * @return String the node name of the product model page corresponding to the provided path
     */
    public static String getProductNameFromPath(String pagePath) {
        String[] path = pagePath.split("/");
        return path[path.length - 1];
    }
}
