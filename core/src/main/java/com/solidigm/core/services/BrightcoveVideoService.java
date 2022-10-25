package com.solidigm.core.services;

public interface BrightcoveVideoService {
    /**
     * Returns the default video to play if no id is entered by component author
     * @return String
     */
    String getVideoId();

    /**
     * Returns Brightcove account id
     * @return String
     */
    String getAccountId();

    /**
     * Returns id for video player
     * @return String
     */
    String getPlayerId();

}
