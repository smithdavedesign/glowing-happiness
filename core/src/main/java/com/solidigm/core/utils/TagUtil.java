package com.solidigm.core.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TagUtil {
    /**
     * Encodes a tag ID so that it can be used as a selector in the URI. Specifically replaces all '/' with '--',
     * then encodes the resulting string with URLEncoder.
     * @param tagID the tag ID to be encoded
     * @return String an encoded tag ID
     */
    public static String encodeID(String tagID){
        return URLEncoder.encode(tagID.replaceAll("/", "--"), StandardCharsets.UTF_8);
    }

    /**
     * Decodes an encoded tag ID so that it can be used by AEM APIs. Specifically decodes the URI string with URLDecoder,
     * then replaces all '--' with '/'.
     * @param encodedID the encoded ID to be decoded
     * @return String a decoded tag ID
     */
    public static String decodeID(String encodedID){
        return URLDecoder.decode(encodedID, StandardCharsets.UTF_8).replaceAll("--", "/");
    }
}
