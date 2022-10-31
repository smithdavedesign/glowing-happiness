package com.solidigm.core.beans;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class OrderingSpec {
    @Inject
    @Getter
    private String name;

    @Getter
    @Inject
    @Named("./PCode/value")
    private String ccats;

    @Getter
    @Inject
    @Named("./ECC/value")
    private String eccn;

    @Getter
    @Inject
    @Named("./HTS/value")
    private String ushts;

    @Getter
    @Inject
    @Named("./OrderingCode/value")
    private String orderingCode;

    @Getter
    @Inject
    @Named("./ShippingMedia/value")
    private String shippingMedia;
}
