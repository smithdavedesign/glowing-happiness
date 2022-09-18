package com.solidigm.core.beans;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductVariation {
    @Inject
    private Resource resource;

    @Inject
    @Getter
    private String name;

    @Inject
    @Getter
    private String brand;

    @Getter
    private String variationID;

    @Getter
    @Inject
    @Named("./attributes/SSDInterface/value")
    private String productInterface;

    @Getter
    @Inject
    @Named("./attributes/SSDFormFactor/value")
    private String formFactor;

    @Getter
    @Inject
    @Named("./attributes/DriveCapacity/value")
    private String capacity;

    @Getter
    @Inject
    @Named("./attributes/DriveCapacity/rawValue")
    private int capacityRaw;

    @PostConstruct
    private void init() {
        if (resource != null) {
            variationID = resource.getName();
        }
    }
}
