package com.solidigm.core.beans;

import com.solidigm.core.services.ProductService;
import com.solidigm.core.utils.ImageUtil;
import com.solidigm.core.utils.SolidigmUtils;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import com.day.cq.wcm.api.Page;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductPage {
    @Inject
    ResourceResolver resourceResolver;

    @OSGiService
    ProductService productService;

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

    public ProductPage(String productPagePath, ResourceResolver resourceResolver, ProductService productService) {
        this.productPagePath = productPagePath;
        this.resourceResolver = resourceResolver;
        this.productService = productService;
        init();
    }

    @PostConstruct
    private void init() {
        if (productPagePath != null) {
            Resource productPageResource = resourceResolver.getResource(productPagePath);
            if (productPageResource != null) {
                //Set teaser information from the page properties
                Page productPage = productPageResource.adaptTo(Page.class);
                name = productPage.getTitle();
                description = productPage.getDescription();

                //Set Teaser image and alt text
                Resource productImage = productPageResource.getChild("jcr:content").getChild("cq:featuredimage");
                if (productImage != null) {
                    image = productImage.getValueMap().get("fileReference", String.class);
                    if (Boolean.TRUE.equals(productImage.getValueMap().get("altValueFromDAM", Boolean.class))) {
                        altText = ImageUtil.getAltTextFromDAM(resourceResolver, image);
                    } else {
                        altText = productImage.getValueMap().get("alt", String.class);
                    }
                }

                //Set teaser product info
                ProductModel productProperties = productService.getProduct(SolidigmUtils.getProductCategoryFromPath(productPagePath),
                                                                           SolidigmUtils.getProductSeriesFromPath(productPagePath),
                                                                           SolidigmUtils.getProductNameFromPath(productPagePath));
                productInterface = String.join(" / ", productProperties.getInterfaces());
                formFactor = String.join(" / ", productProperties.getFormFactors());
                List<String> capacities = productProperties.getCapacities();
                if (capacities.size() > 1) {
                    capacity = capacities.get(0).concat(" - ").concat(capacities.get(capacities.size() - 1));
                } else if (!capacities.isEmpty()) {
                    capacity = capacities.get(0);
                }
            }
        }
    }

}
