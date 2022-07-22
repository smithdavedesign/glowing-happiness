package com.solidigm.core.services;

public interface SocialService {
    /**
     * Returns the url for twitter sharing that can have the external link to the page to be shared appended to it
     */
    default String getTwitterShareUrl() { throw new UnsupportedOperationException(); }

    /**
     * Returns the url for LinkedIn sharing that can have the external link to the page to be shared appended to it
     */
    default String getLinkedInShareUrl() { throw new UnsupportedOperationException(); }
}
