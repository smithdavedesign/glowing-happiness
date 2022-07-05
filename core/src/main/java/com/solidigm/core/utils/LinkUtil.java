package com.solidigm.core.utils;

import com.drew.lang.annotations.NotNull;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

public class LinkUtil {
    /**
     * Returns the String of the Alt text stored in the DAM of the provided image
     *
     * @param String Link
     * @return String the link with ".html" appended to the end if needed or the external link as is
     */
    public static String processLink(String link) {
        if (!StringUtils.isEmpty(link) && link.startsWith("/") && StringUtils.isEmpty(FilenameUtils.getExtension(link))) {
                link = link.concat(".html");
        }
        return link;
    }

    /**
     * Returns the String of the Alt text stored in the DAM of the provided image
     *
     * @param String Link
     * @return String the new target value depending on if the link is internal or external
     */
    public static String processTarget(String link) {
        if (!StringUtils.isEmpty(link) && link.startsWith("/")) {
            return "_self";
        }
        return "_blank";
    }
}