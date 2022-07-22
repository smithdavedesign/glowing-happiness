package com.solidigm.core.models.impl;

import org.apache.sling.models.annotations.Model;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.tagging.TagManager;
import com.solidigm.core.beans.Link;
import com.solidigm.core.models.Footer;

import lombok.Getter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.day.cq.tagging.Tag;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {Footer.class, ComponentExporter.class},
        resourceType = FooterImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class FooterImpl implements Footer{
    static final String RESOURCE_TYPE = "solidigm/components/footer";

    @SlingObject
    SlingHttpServletRequest request;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
    
    @Getter
    @ValueMapValue
    private String searchTitle;

    @Getter
    @ValueMapValue
    private String placeholderText;

    @Getter
    @ValueMapValue
    private String searchResultsPage;

    @Getter
    @ValueMapValue
    private String disclaimer;

    @Getter
    @ValueMapValue
    private String trademark;

    @Getter
    @ValueMapValue
    private String copyright;

    @Getter
    @ChildResource(name = "links")
    private List<Link> links;

    @ValueMapValue(name = "homePage")
    private String homePageLink;

    @ValueMapValue(name = "facebook")
    private String facebookLink;

    @ValueMapValue(name = "linkedIn")
    private String linkedInLink;

    @ValueMapValue(name = "trustArc")
    private String trustArcLink;

    @ValueMapValue
    private String[] searchTags;

    @Getter
    private Link homePage;

    @Getter
    private Link facebook;

    @Getter
    private Link linkedIn;

    @Getter
    private Link trustArc;

    @Getter
    private List<Link> searchTagsLinks;

    @PostConstruct
    private void initModel() {
        //Initialize variables
        searchTagsLinks = new ArrayList<>();
        homePage = new Link();
        facebook = new Link();
        linkedIn = new Link();
        trustArc = new Link();

        homePage.setLink(homePageLink);
        facebook.setLink(facebookLink);
        linkedIn.setLink(linkedInLink);
        trustArc.setLink(trustArcLink);

        if (searchTags == null || searchTags.length == 0){
            return;
        }

        ResourceResolver resourceResolver = request.getResourceResolver();
        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

        //Resolve tags by their shorthand path to get their titles and add the title + shorthand path to a list of links
        for (String searchTag : searchTags) {
            Tag tag = tagManager.resolve(searchTag);
            String tagLabel = tag.getTitle();
            String tagLink = searchResultsPage.concat(".html?tag=")
                                .concat(URLEncoder.encode(tag.getTagID(), StandardCharsets.UTF_8));
            Link link = new Link();
            link.setLink(tagLink);
            link.setLabel(tagLabel);
            searchTagsLinks.add(link);
        }
    }
}
