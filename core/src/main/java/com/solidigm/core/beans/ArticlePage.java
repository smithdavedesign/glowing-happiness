package com.solidigm.core.beans;

import lombok.Getter;

public class ArticlePage {
    @Getter
    private String title;

    @Getter
    private String image;

    @Getter
    private String category;

    @Getter
    private String path;

    public ArticlePage(String title, String image, String category, String path) {
        this.title = title;
        this.image = image;
        this.category = category;
        this.path = path;
    }
}
