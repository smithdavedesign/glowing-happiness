package com.solidigm.core.services;

public class BrightcoveVideoService {
    /**
     * Returns the default video to play if no id is entered by component author
     * @return String
     */
    String getVideoPlayerId();

    /**
     * Returns Brightcove account id
     * @return String
     */
    String getAccountId();

    /**
     * Takes the KA's API response JSON and processes it. Updating, creating, and deleting KA pages in AEM
     * @param articleJson
     */
    void processArticles(String articleJson);
}
