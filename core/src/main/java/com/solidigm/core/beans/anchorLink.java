package com.solidigm.core.beans;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class anchorLink {
    @Getter
    @Inject
    private String anchorLabel;

    @Getter
    @Inject
    private String anchorID;
}
