package com.solidigm.core.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductModel {
    @Inject
    private Resource resource;

    @Getter
    private String name;

    @Getter
    private String brand;

    @Getter
    private String productGroup;

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
        //Init variables
        capacities = new ArrayList<>();
        formFactors = new ArrayList<>();
        interfaces = new ArrayList<>();
        variations = new HashMap<>();
        List<CapacitySortable> unsortedCapacities = new ArrayList<>();

        if (resource != null) {
            for (Resource variationResource : resource.getChildren()) {
                ProductVariation variation = variationResource.adaptTo(ProductVariation.class);
                //Set model wide values
                if (StringUtils.isEmpty(name)) {
                    name = variation.getName();
                }
                if (StringUtils.isEmpty(brand)) {
                    brand = variation.getBrand();
                }
                if (StringUtils.isEmpty(productGroup)){
                    productGroup = variation.getProductGroup();
                }

                //Add form factors and interfaces for the model if they haven't already been added
                if (!formFactors.contains(variation.getFormFactor())) {
                    formFactors.add(variation.getFormFactor());
                }
                if (!interfaces.contains(variation.getProductInterface())) {
                    interfaces.add(variation.getProductInterface());
                }

                //Add capacity to list to be sorted
                unsortedCapacities.add(new CapacitySortable(variation.getCapacityRaw(), variation.getCapacity()));

                //Add variation to product model map of variations
                variations.put(variation.getVariationID(), variation);
            }
        }

        //Sort variation capacities and add unique capacities to model capacities
        unsortedCapacities.sort(new SortCapacities());
        for (CapacitySortable capacity : unsortedCapacities) {
            if(!capacities.contains(capacity.getCapacity())) {
                capacities.add(capacity.getCapacity());
            }
        }
    }

    private static class CapacitySortable {
        @Getter
        @Setter
        private int rawCapacity;

        @Getter
        @Setter
        private String capacity;

        public CapacitySortable(int rawCapacity, String capacity) {
            this.rawCapacity = rawCapacity;
            this.capacity = capacity;
        }
    }

    private static class SortCapacities implements Comparator<CapacitySortable> {
        public int compare(CapacitySortable a, CapacitySortable b)
        {
            return a.getRawCapacity() - b.getRawCapacity();
        }
    }
}
