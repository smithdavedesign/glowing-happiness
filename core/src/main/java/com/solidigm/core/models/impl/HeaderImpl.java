package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.solidigm.core.beans.Link;
import com.solidigm.core.models.Header;
import lombok.Getter;
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

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @Getter
    @ValueMapValue
    private String navItemTitle1;

    @Getter
    @ValueMapValue
    private String navItemTitle2;

    @Getter
    @ValueMapValue
    private String navItemTitle3;

    @Getter
    @ValueMapValue
    private String navItemTitle4;

    @Getter
    @ValueMapValue
    private String navItemTitle5;

    @Getter
    @ValueMapValue
    private String navItemLink1;

    @Getter
    @ValueMapValue
    private String navItemLink2;

    @Getter
    @ValueMapValue
    private String navItemLink3;

    @Getter
    @ValueMapValue
    private String navItemLink4;

    @Getter
    @ValueMapValue
    private String navItemLink5;

    @Getter
    @ChildResource(name="subNavItems1")
    private List<Link> subNavItems1;

    @Getter
    @ChildResource(name="subNavItems2")
    private List<Link> subNavItems2;

    @Getter
    @ChildResource(name="subNavItems3")
    private List<Link> subNavItems3;

    @Getter
    @ChildResource(name="subNavItems4")
    private List<Link> subNavItems4;

    @Getter
    @ChildResource(name="subNavItems5")
    private List<Link> subNavItems5;

    @Getter
    @ChildResource(name="languageInfo")
    private List<Link> langItems;

    @Getter
    @ValueMapValue
    private String greeting;

    @Getter
    @ValueMapValue
    private String searchBarPlaceholder;

    @Getter
    @ValueMapValue
    private String searchBarSearchResultsPage;

    @PostConstruct
    private void initModel() {
        String l = "hmm";
    }
}
