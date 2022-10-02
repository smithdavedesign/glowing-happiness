package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.solidigm.core.beans.Partner;
import com.solidigm.core.models.PartnerList;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {PartnerList.class, ComponentExporter.class},
        resourceType = PartnerListImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class PartnerListImpl implements PartnerList {
    static final String RESOURCE_TYPE = "solidigm/components/partnerlist";
    private static final String COUNTRY_TAGS_ROOT = "/content/cq:tags/solidigm/partners/countries";
    private static final String PRODUCT_TAGS_ROOT = "/content/cq:tags/solidigm/partners/products";

    @SlingObject
    ResourceResolver resourceResolver;

    @ValueMapValue
    @Getter
    private String partnerType;

    @ChildResource(name = "partners")
    @Getter
    private List<Partner> partners;

    @Getter
    private List<Tag> countries;

    @Getter
    private List<Tag> products;

    private Boolean hasContentVal;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @Override
    public Boolean hasContent() {
        return hasContentVal;
    }

    @PostConstruct
    private void initModel() {
        //Init Variables
        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
        countries = new ArrayList<>();
        products = new ArrayList<>();

        //Set Country Name for partners
        if(partners != null && tagManager != null) {
            for (Partner partner : partners) {
                partner.setCountryName(tagManager.resolve(partner.getCountryID()).getTitle());
            }
        }

        Resource countriesResource = resourceResolver.getResource(COUNTRY_TAGS_ROOT);
        if(countriesResource != null) {
            for(Resource country : countriesResource.getChildren()) {
                countries.add(country.adaptTo(Tag.class));
            }
        }

        Resource productResource = resourceResolver.getResource(PRODUCT_TAGS_ROOT);
        if(productResource != null) {
            for(Resource product : productResource.getChildren()) {
                products.add(product.adaptTo(Tag.class));
            }
        }

        hasContentVal = StringUtils.isNotEmpty(partnerType) && partners != null && !partners.isEmpty();
    }
}
