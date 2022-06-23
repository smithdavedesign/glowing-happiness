package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.solidigm.core.models.Header;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.List;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {Header.class, ComponentExporter.class},
        resourceType = HeaderImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class HeaderImpl implements Header {
    static final String RESOURCE_TYPE = "solidigm/components/header";

    private static final Boolean EMPTY_SELECTION_SHOW_BREADCRUMB = false;

    private static final String EMPTY_SELECTION_FILTER = "no_filter";

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
    private String desktopImageReference;

    @Getter
    @ValueMapValue
    private String mobileImageReference;

    @ValueMapValue
    private String breadcrumb;

    @ValueMapValue(name = "filter")
    private String filterSelected;

    @Getter
    private Boolean showBreadcrumb;

    @Getter
    private String filter;

    @Getter
    private Boolean hasContent;

    @PostConstruct
    private void initModel() {
        showBreadcrumb = StringUtils.isEmpty(breadcrumb) == EMPTY_SELECTION_SHOW_BREADCRUMB;
        filter = StringUtils.isEmpty(filterSelected) ? EMPTY_SELECTION_FILTER : filterSelected;
        hasContent= !(StringUtils.isEmpty(desktopImageReference) || StringUtils.isEmpty(mobileImageReference));
    }
}
