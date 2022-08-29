package com.solidigm.core.beans;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductPage {
    @Inject
    ResourceResolver resourceResolver;

    @Inject
    @Getter
    private String productPagePath;

    @Getter
    private String name;

    @Getter
    private String description;

    @Getter
    private String productInterface;

    @Getter
    private String formFactor;

    @Getter
    private String capacity;

    @Getter
    private String image;

    @Getter String altText;

    public ProductPage() {
    }

    public ProductPage(String productPagePath, ResourceResolver resourceResolver) {
        this.productPagePath = productPagePath;
        this.resourceResolver = resourceResolver;
        init();
    }

    @PostConstruct
    private void init() {
        if (productPagePath != null) {
            Resource productPageResource = resourceResolver.getResource(productPagePath);
        }

        name = "Test: D7-P5600";
        description = "Test: Balanced read/write performance for mixed workload applications. Available in 1.6 TB, 3.2 TB, and 6.4 TB capacities, the D7-P5600 is a strong blend of performance, endurance, and capacity.";
        productInterface = "Test: PCIE 4.0 x4 NVME";
        formFactor = "Test: U.2 15MM";
        capacity = "Test: 2MB";
        image = "/content/dam/solidigm/master/products/d3/s4520/d3-s4520.png";
        altText = "test: alt text";
    }

}
