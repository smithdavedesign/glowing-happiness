package com.solidigm.core.services;

public interface DynamicsArticlesService {
    /**
     * Fetches the API token from microsoft
     * @return String
     */
    String acquireToken();

    /**
     * Fetches the API response JSON and returns it as a String
     * @param accessToken - The login token acquired from Microsoft
     * @return
     */
    String fetchArticles(String accessToken);

    /**
     * Takes the KA's API response JSON and processes it. Updating, creating, and deleting KA pages in AEM
     * @param articleJson
     */
    void processArticles(String articleJson);
}

