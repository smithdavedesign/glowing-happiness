package com.solidigm.core.beans;

import java.util.Date;

public class KnowledgeArticle {

    private String articleId;
    private String title;
    private String description;
    private String content;
    private String languagelocaleIdValue;
    private String createdOn;
    private String modifiedOn;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLanguagelocaleIdValue() {
        return languagelocaleIdValue;
    }

    public void setLanguagelocaleIdValue(String languagelocaleIdValue) {
        this.languagelocaleIdValue = languagelocaleIdValue;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

}
