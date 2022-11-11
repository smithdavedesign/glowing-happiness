package com.solidigm.core.beans;

import lombok.Getter;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Filter {
    @Inject
    @Getter
    private String label;

    @Inject
    @Getter
    private String filterPath;

    @Getter
    private String id;

    @PostConstruct
    private void init() {
        id = label.toLowerCase();
    }
}
