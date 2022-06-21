package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.solidigm.core.beans.anchorLink;
import com.solidigm.core.models.Hero;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.List;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {Hero.class, ComponentExporter.class},
        resourceType = HeroImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class HeroImpl implements Hero {
    static final String RESOURCE_TYPE = "solidigm/components/hero";

    private static final String SHOW_BREADCRUMB = "show_breadcrumb";

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @Getter
    @ValueMapValue
    private String title;

    @Getter
    @ValueMapValue
    private String subtitle;

    @Getter
    @ValueMapValue
    private String description;

    @Getter
    @ValueMapValue
    private String breadcrumb;

    @Getter
    @ValueMapValue
    private String desktopImageReference;

    @Getter
    @ValueMapValue
    private String mobileImageReference;

    @Getter
    @ValueMapValue
    private String filter;

    @Getter
    @ValueMapValue
    private String cta1Text;

    @Getter
    @ValueMapValue
    private String cta1Link;

    @Getter
    @ValueMapValue
    private String cta2Text;

    @Getter
    @ValueMapValue
    private String cta2Link;

    @Getter
    @ChildResource(name = "anchors")
    private List<anchorLink> anchors;

    @Getter
    private Boolean showBreadcrumb;

    @Override
    public Boolean isEmpty() {
        return (StringUtils.isEmpty(desktopImageReference) || StringUtils.isEmpty(mobileImageReference));
    }

    @PostConstruct
    private void initModel() {
        showBreadcrumb = !StringUtils.isEmpty(breadcrumb) && breadcrumb.equals(SHOW_BREADCRUMB);
    }
}
