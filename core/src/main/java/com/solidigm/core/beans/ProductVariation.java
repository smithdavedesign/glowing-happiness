package com.solidigm.core.beans;

import com.google.gson.JsonObject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductVariation {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
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
    @Named("./attributes/LatencySequentialRead/value")
    private String sequentialReadLatency;

    @Getter
    @Inject
    @Named("./attributes/LatencySequentialWriteValue/value")
    private String sequentialWriteLatency;

    @Getter
    @Inject
    @Named("./attributes/MTBF/value")
    private String mtbf;

    @Getter
    @Inject
    @Named("./attributes/NANDTechnology/value")
    private String nandTechnology;

    @Getter
    @Inject
    @Named("./attributes/OperatingTemperature/value")
    private String operatingTemperature;

    @Getter
    @Inject
    @Named("./attributes/RandomRead100PctSpanValue/value")
    private String randomRead;

    @Getter
    @Inject
    @Named("./attributes/RandomWrite100PctSpanValue/value")
    private String randomWrite;

    @Getter
    @Inject
    @Named("./attributes/SequentialReadMB/value")
    private String sequentialRead;

    @Getter
    @Inject
    @Named("./attributes/SequentialWriteMB/value")
    private String sequentialWrite;

    @Getter
    @Inject
    @Named("./attributes/SSDEnduranceRating/value")
    private String endurance;

    @Getter
    @Inject
    @Named("./attributes/SSDPowerActive/value")
    private String powerActive;

    @Getter
    @Inject
    @Named("./attributes/SSDPowerIdle/value")
    private String powerIdle;

    @Getter
    @Inject
    @Named("./attributes/SSDShock/value")
    private String shock;

    @Getter
    @Inject
    @Named("./attributes/SSDWeight/value")
    private String weight;

    @Getter
    @Inject
    @Named("./attributes/UBER/value")
    private String uber;

    @Getter
    @Inject
    @Named("./attributes/VibrationNonOperating/value")
    private String vibrationNonOperating;

    @Getter
    @Inject
    @Named("./attributes/VibrationOperating/value")
    private String vibrationOperating;

    @Getter
    @Inject
    @Named("./attributes/WarrantyLengthYears/value")
    private String warrantyLength;

    @Getter
    @Inject
    @Named("./attributes/SSDHardwareEncryption/value")
    private String hardwareEncryption;

    @Getter
    @Inject
    @Named("./attributes/EndToEndDataProtection/value")
    private String dataProtection;

    @Getter
    @Inject
    @Named("./attributes/SSDHighEnduranceTechnology/value")
    private String het;

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
        log.debug("This should be the product capacity:".concat(capacity));
        if (resource != null) {
            variationID = resource.getName();
        }
    }
}
