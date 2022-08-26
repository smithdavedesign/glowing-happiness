package com.solidigm.core.utils;

import org.apache.commons.lang.StringUtils;

import static com.solidigm.core.utils.SolidigmConstants.HOME_NAVIGATION_LEVEL;

public class SolidigmUtils {

    /**
     * Finds the path of the site root of the provided page. E.g. /content/solidigm/us/en
     *
     * @param pagePath the path of the current page
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
}
