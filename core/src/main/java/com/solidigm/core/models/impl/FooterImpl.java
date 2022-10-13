package com.solidigm.core.models.impl;

import com.day.cq.wcm.api.Page;
import com.solidigm.core.models.Header;
import com.solidigm.core.utils.SolidigmUtils;
import com.solidigm.core.utils.TagUtil;
import org.apache.sling.models.annotations.Model;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.tagging.TagManager;
import com.solidigm.core.beans.Link;
import com.solidigm.core.models.Footer;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.day.cq.tagging.Tag;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.osgi.service.component.annotations.Reference;

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

    @Inject
    @Source("script-bindings")
    Page currentPage;

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
    @Getter
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
        //If this is not the parent footer from the root page, get values from that instead
        if (currentPage != null && request != null &&
                !currentPage.getPath().equals(SolidigmUtils.getSiteRoot(currentPage.getPath()))) {
            ResourceResolver resourceResolver = request.getResourceResolver();
            Resource parentFooterResource = resourceResolver.getResource(
                    SolidigmUtils.getSiteRoot(currentPage.getPath()).concat("/jcr:content/root/footer"));
            if (parentFooterResource != null) {
                Footer parentFooter = parentFooterResource.adaptTo(Footer.class);
                if (parentFooter != null) {
                    this.links = parentFooter.getLinks();
                    this.searchTitle = parentFooter.getSearchTitle();
                    this.placeholderText = parentFooter.getPlaceholderText();
                    this.searchResultsPage = parentFooter.getSearchResultsPage();
                    this.disclaimer = parentFooter.getDisclaimer();
                    this.trademark = parentFooter.getTrademark();
                    this.copyright = parentFooter.getCopyright();
                    this.homePage = parentFooter.getHomePage();
                    this.facebook = parentFooter.getFacebook();
                    this.linkedIn = parentFooter.getLinkedIn();
                    this.trustArc = parentFooter.getTrustArc();
                    this.links = parentFooter.getLinks();
                    this.searchTags = parentFooter.getSearchTags();
                }
            }
        } else {
            //Initialize variables
            homePage = new Link();
            facebook = new Link();
            linkedIn = new Link();
            trustArc = new Link();

            homePage.setLink(homePageLink);
            facebook.setLink(facebookLink);
            linkedIn.setLink(linkedInLink);
            trustArc.setLink(trustArcLink);
        }

        searchTagsLinks = new ArrayList<>();
        if (searchTags == null || searchTags.length == 0 || request == null) {
            return;
        }

        ResourceResolver resourceResolver = request.getResourceResolver();
        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

        //Resolve tags by their shorthand path to get their titles and add the title + shorthand path to a list of links
        if (tagManager != null) {
            for (String searchTag : searchTags) {
                Tag tag = tagManager.resolve(searchTag);
                String tagLabel = tag.getTitle();
                String tagLink = new Link("", searchResultsPage).getLink().concat("?query=")
                        .concat(tagLabel);
                Link link = new Link();
                link.setLink(tagLink);
                link.setLabel(tagLabel);
                searchTagsLinks.add(link);
            }
        }

    }
}
