package com.solidigm.core.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticlePage {
    @Inject
    @Getter
    private String articleTitle;

    @Inject
    @Getter
    private String articleCategory;

    @Inject
    @Named("date")
    @Getter
    private Date articleDate;

    @Inject
    @Getter
    private Date startDate;

    @Inject
    @Getter
    private Date startTime;

    @Inject
    @Getter
    private Date endDate;

    @Inject
    @Getter
    private Date endTime;

    @Inject
    @Getter
    private String articleAuthor;

    @Inject
    @Getter
    private String eventLocation;

    @Inject
    @Getter
    private String eventInfo;

    @Inject
    @Getter
    private String teaserImage;

    @Inject
    @Named("jcr:description")
    @Getter
    private String description;

    @Inject
    @Getter
    @Setter
    private String teaserImageAltText;

    @Inject
    @Named("cq:tags")
    @Getter
    private String[] articleTags;

    @Getter
    @Setter
    private String path;

    @Getter
    @Setter
    private String teaserCategory;
}
