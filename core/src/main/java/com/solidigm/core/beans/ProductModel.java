package com.solidigm.core.beans;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductModel {
    @Inject
    private Resource resource;

    @Getter
    private String name;

    @Getter
    private String brand;

    @Getter
    private List<String> capacities;

    @Getter
    private List<String> formFactors;

    @Getter
    private List<String> interfaces;

    @Getter
    private Map<String, ProductVariation> variations;

    @PostConstruct
    private void init() {
        capacities = new ArrayList<>();
        formFactors = new ArrayList<>();
        interfaces = new ArrayList<>();
        variations = new HashMap<>();

        if (resource != null) {
            for (Resource variationResource : resource.getChildren()) {
                ProductVariation variation = variationResource.adaptTo(ProductVariation.class);
                if (StringUtils.isEmpty(name)) {
                    name = variation.getName();
                }
                if (StringUtils.isEmpty(brand)) {
                    brand = variation.getBrand();
                }
                if (!capacities.contains(variation.getCapacity())) {
                    capacities.add(variation.getCapacity());
                }
                if (!formFactors.contains(variation.getFormFactor())) {
                    formFactors.add(variation.getFormFactor());
                }
                if (!interfaces.contains(variation.getProductInterface())) {
                    interfaces.add(variation.getProductInterface());
                }
                variations.put(variation.getVariationID(), variation);
            }
        }
    }
}
