package com.solidigm.core.beans;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

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
    private String productInterface;

    @Getter
    private String formFactor;

    @Getter
    private String capacity;

    @PostConstruct
    private void init() {
        if (resource != null) {
            variationID = resource.getName();
            Resource attributes = resource.getChild("attributes");
            if (attributes != null) {
                Resource driveCapacity = attributes.getChild("DriveCapacity");
                if (driveCapacity != null) {
                    capacity = driveCapacity.getValueMap().get("value", String.class);
                }
                Resource driveFormFactor = attributes.getChild("SSDFormFactor");
                if (driveCapacity != null) {
                    formFactor = driveFormFactor.getValueMap().get("value", String.class);
                }
                Resource driveInterface = attributes.getChild("SSDInterface");
                if (driveCapacity != null) {
                    productInterface = driveInterface.getValueMap().get("value", String.class);
                }
            }
        }
    }
}
