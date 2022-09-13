package com.solidigm.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Solidigm Product Service Configuration",
        description = "Configure various properties used by the Product Service, which populates PDP pages with data from the JCR"
)
public @interface ProductServiceConfig {
    @AttributeDefinition(
            name = "Product Data Path",
            description = "Path to the product data stored in the JCR")
    String getProductDataPath() default "/var/commerce/products/solidigm";
}
