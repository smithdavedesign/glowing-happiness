package com.solidigm.core.utils;

import org.apache.commons.lang.StringUtils;

import static com.solidigm.core.utils.SolidigmConstants.HOME_NAVIGATION_LEVEL;

public class SolidigmUtils {
    public static String getSiteRoot(String pagePath) {
        int endIndex = StringUtils.ordinalIndexOf(pagePath, "/", HOME_NAVIGATION_LEVEL);
        if(endIndex != -1) {
            return pagePath.substring(0, endIndex);
        } else {
            return pagePath;
        }
    }
}
