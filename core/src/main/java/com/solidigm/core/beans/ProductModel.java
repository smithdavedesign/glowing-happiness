package com.solidigm.core.beans;

import lombok.Getter;
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
    private List<CapacitySortable> capacitiesSortable;

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
        Map<String, CapacitySortable> unsortedCapacities = new HashMap<>();
        Map<String, VariationSortable> unsortedVariations = new HashMap<>();

        if (resource != null) {
            for (Resource variationResource : resource.getChildren()) {
                ProductVariation variation = variationResource.adaptTo(ProductVariation.class);
                if (variation != null) {
                    //Set model wide values
                    if (StringUtils.isEmpty(name)) {
                        name = variation.getName();
                    }
                    if (StringUtils.isEmpty(brand)) {
                        brand = variation.getBrand();
                    }
                    if (StringUtils.isEmpty(productGroup)) {
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
                    unsortedCapacities.put(variation.getCapacity(),
                                           new CapacitySortable(variation.getCapacityRaw(), variation.getCapacity()));

                    //Add variation to product model map of variations
                    unsortedVariations.put(variation.getVariationID(),
                                           new VariationSortable(variation, variation.getCapacityRaw()));
                }
            }
        }

        //Sort variation capacities and add unique capacities to model capacities
        capacitiesSortable = new ArrayList<>(unsortedCapacities.values());
        capacitiesSortable.sort(new CapacitySortable.SortCapacities());
        for (CapacitySortable capacity : capacitiesSortable) {
            capacities.add(capacity.getCapacity());
        }

        // Sort variation hashmap into order
        // Use LinkedHashMap to maintain order
        LinkedHashMap<String, ProductVariation> linkedVariations = new LinkedHashMap<>();
        List<VariationSortable> variationsSortable = new ArrayList<>(unsortedVariations.values());
        variationsSortable.sort(new VariationSortable.SortVariations());
        for (VariationSortable variation : variationsSortable) {
            linkedVariations.put(variation.getVariation().getVariationID(), variation.getVariation());
        }
        variations = linkedVariations;
    }
}
