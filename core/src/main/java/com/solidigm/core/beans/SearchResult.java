package com.solidigm.core.beans;

import lombok.Getter;

public class SearchResult {
    @Getter
    private String pagePath;

    @Getter
    private String title;

    @Getter
    private String description;

    @Getter
    private String url;

    @Getter
    private String image;

    @Getter
    private String altText;

    public SearchResult(String pagePath, String title, String description, String url, String image, String altText) {
        this.pagePath = pagePath;
        this.title = title;
        this.description = description;
        this.url = url;
        this.image = image;
        this.altText = altText;
    }
}
