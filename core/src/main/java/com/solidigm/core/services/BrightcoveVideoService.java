package com.solidigm.core.services;

public interface BrightcoveVideoService {

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
