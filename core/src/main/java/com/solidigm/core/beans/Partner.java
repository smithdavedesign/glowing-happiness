package com.solidigm.core.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Partner {
    @Inject
    @Getter
    private String companyName;

    @Inject
    @Named("country")
    @Getter
    private String countryID;

    @Getter
    @Setter
    private String countryName;

    @Inject
    @Getter
    private String additionalInfo;

    @Inject
    @Getter
    private String[] products;

    @Inject
    @Getter
    private String link;
}
