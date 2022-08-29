package com.solidigm.core.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BioItem {
    @Inject
    private Resource resource;

    @Inject
    @Getter
    private String imageReference;

    @Inject
    @Getter
    @Setter
    private String imageAltText;

    @Inject
    @Getter
    private String name;

    @Inject
    @Getter
    private String title;

    @Inject
    @Getter
    private String description;

    @Getter
    private String modalID;

    @PostConstruct
    private void init() {
        if(resource != null) {
            modalID = String.valueOf(Math.abs(resource.getPath().hashCode()));
        } else {
            modalID = String.valueOf(Math.floor(Math.random() * 100000));
        }
    }
}
