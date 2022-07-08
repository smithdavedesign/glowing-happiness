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

    @ValueMapValue
    private String navItemTitle1;

    @ValueMapValue
    private String navItemTitle2;

    @ValueMapValue
    private String navItemTitle3;

    @ValueMapValue
    private String navItemTitle4;

    @ValueMapValue
    private String navItemTitle5;

    @ValueMapValue
    private String navItemLink1;

    @ValueMapValue
    private String navItemLink2;

    @ValueMapValue
    private String navItemLink3;

    @ValueMapValue
    private String navItemLink4;

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

    @ValueMapValue(name = "searchResultsPage")
    private String searchResultsPagePath;

    @ValueMapValue(name = "dashboard")
    private String dashboardPath;

    @ValueMapValue(name = "homepage")
    private String homepagePath;

    @ValueMapValue
    private String homepageLabel;

    @Getter
    @ChildResource(name="langItems")
    private List<Link> langItems;

    @Getter
    @ValueMapValue
    private String greeting;

    @Getter
    @ValueMapValue
    private String placeholderText;

    @Getter
    private Link navItem1;
    @Getter
    private Link navItem2;
    @Getter
    private Link navItem3;
    @Getter
    private Link navItem4;
    @Getter
    private Link navItem5;

    @Getter
    private Link searchResultsPage;

    @Getter
    private Link dashboard;

    @Getter
    private Link homepage;

    @PostConstruct
    private void initModel() {
        //Init variables
        navItem1 = new Link(navItemTitle1, navItemLink1);
        navItem2 = new Link(navItemTitle2, navItemLink2);
        navItem3 = new Link(navItemTitle3, navItemLink3);
        navItem4 = new Link(navItemTitle4, navItemLink4);
        navItem5 = new Link(navItemTitle5, navItemLink5);
        searchResultsPage = new Link("", searchResultsPagePath);
        dashboard = new Link("", dashboardPath);
        homepage = new Link(homepageLabel, homepagePath);
    }
}
