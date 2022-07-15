package com.solidigm.core.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticlePage {
    @Inject
    @Getter
    private String articleTitle;

    @Inject
    @Getter
    private String articleCategory;

    @Inject
    @Getter
    private String articleDate;

    @Inject
    @Getter
    private String teaserImage;

    @Inject
    @Getter
    @Setter
    private String teaserImageAltText;

    @Getter
    @Setter
    private String path;

    @Getter
    @Setter
    private String teaserCategory;
}
