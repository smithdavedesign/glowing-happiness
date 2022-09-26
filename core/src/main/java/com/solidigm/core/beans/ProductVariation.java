package com.solidigm.core.beans;

import com.google.gson.JsonObject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
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

    @Getter
    @Inject
    @Named("./attributes/ProductGroup/value")
    private String productGroup;

    @Getter
    @ChildResource
    private List<OrderingSpec> orderingInfo;

    @Inject
    @Named("./attributes")
    private Resource attributesResource;

    //Returns all the product attributes as a JSON
    public String getAttributesJSON() {
        JsonObject json = new JsonObject();
        if (attributesResource != null) {
            for (Resource attribute : attributesResource.getChildren()) {
                json.addProperty(attribute.getName().replaceAll("\\W*", ""), attribute.getValueMap().get("value", String.class));
            }
        }
        return json.toString();
    }

    @PostConstruct
    private void init() {
        if (resource != null) {
            variationID = resource.getName();
        }
    }
}
