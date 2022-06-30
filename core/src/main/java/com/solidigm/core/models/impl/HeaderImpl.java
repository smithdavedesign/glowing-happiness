package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.xfa.ut.StringUtils;
import com.solidigm.core.beans.Link;
import com.solidigm.core.models.Header;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
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
    static final List<Link> EMPTY_LIST = new ArrayList<>();

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

    @ChildResource(name="subNavItems1")
    private List<Link> subNavItems1;

    @ChildResource(name="subNavItems2")
    private List<Link> subNavItems2;

    @ChildResource(name="subNavItems3")
    private List<Link> subNavItems3;

    @ChildResource(name="subNavItems4")
    private List<Link> subNavItems4;

    @ChildResource(name="subNavItems5")
    private List<Link> subNavItems5;

    @Getter
    @ChildResource(name="langItems")
    private List<Link> langItems;

    @Getter
    @ValueMapValue
    private String greeting;

    @Getter
    @ValueMapValue
    private String placeholderText;

    @ValueMapValue
    private String searchResultsPage;

    @ValueMapValue
    private String dashboard;

    @ValueMapValue
    private String homepage;

    @Getter
    private String target;

    @PostConstruct
    private void initModel() {
        String test = "Testing";
    }

    public String getNavItemLink1() { return processLink(navItemLink1); }

    public String getNavItemLink2() { return processLink(navItemLink2); }

    public String getNavItemLink3() { return processLink(navItemLink3); }

    public String getNavItemLink4() { return processLink(navItemLink4); }

    public String getNavItemLink5() { return processLink(navItemLink5); }

    public List<Link> getSubNavItems1() {
        return (navItemLink1 == null ? subNavItems1 : EMPTY_LIST);
    }

    public List<Link> getSubNavItems2() {
        return (navItemLink2 == null ? subNavItems2 : EMPTY_LIST);
    }

    public List<Link> getSubNavItems3() {
        return (navItemLink3 == null ? subNavItems3 : EMPTY_LIST);
    }

    public List<Link> getSubNavItems4() {
        return (navItemLink4 == null ? subNavItems4 : EMPTY_LIST);
    }

    public List<Link> getSubNavItems5() {
        return (navItemLink5 == null ? subNavItems5 : EMPTY_LIST);
    }

    public String getSearchResultsPage() { return processLink(searchResultsPage); }

    public String getDashboard() { return processLink(dashboard); }

    public String getHomepage() { return processLink(homepage); }

    private String processLink(String link)  {
        if (!StringUtils.isEmpty(link) && link.startsWith("/")){
            target = "_self";
            if (StringUtils.isEmpty(FilenameUtils.getExtension(link))) {
                link = link.concat(".html");
            }
        }
        else if (StringUtils.isEmpty(link)) {
            target = "_self";
        }
        else {
            target = "_blank";
        }
        return link;
    }
}
