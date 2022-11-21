package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.solidigm.core.models.TaggedArticles;
import com.solidigm.core.utils.SolidigmUtils;
import com.solidigm.core.utils.TagUtil;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.solidigm.core.utils.SolidigmConstants.SOLIDIGM_ARTICLE_CATEGORY_TAGS;

@Model(
  adaptables = {Resource.class, SlingHttpServletRequest.class},
  adapters = {TaggedArticles.class, ComponentExporter.class},
  resourceType = TaggedArticlesImpl.RESOURCE_TYPE,
  defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
  name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
  extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class TaggedArticlesImpl implements TaggedArticles {
    static final String RESOURCE_TYPE = "solidigm/components/taggedarticles";

    @SlingObject
    SlingHttpServletRequest request;

    @Inject
    @Source("script-bindings")
    Page currentPage;

    @Getter
    private String rootPath;

    @Getter
    private String selectorTag;

    @Getter
    private List<Tag> dropdownTags;

    @Getter
    private String displayTag;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @PostConstruct
    private void initModel() {
        //Init variables
        ResourceResolver resourceResolver = request.getResourceResolver();
        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
        dropdownTags = new ArrayList<>();

        //Set localized root path
        if(currentPage != null){
            rootPath = SolidigmUtils.getSiteRoot(currentPage.getPath());
        }

        //Decode the selector string if it exists
        String[] selectors = request.getRequestPathInfo().getSelectors();
        if(selectors.length > 0) {
            selectorTag = TagUtil.decodeID(selectors[0]);
            //using tag manager use selector to get tag and display tag
            if (tagManager != null) {
                Tag tag = tagManager.resolve(selectorTag);
                if(tag != null){
                    displayTag = tag.getTitle();
                }
                else{
                    displayTag = "";
                }

            }
        } else {
            selectorTag = "";
            displayTag = "";
        }


        //Get all article categories tags for the dropdown menu
        Resource categoriesResource = request.getResourceResolver().getResource(SOLIDIGM_ARTICLE_CATEGORY_TAGS);
        if(categoriesResource != null) {
            for(Resource categoryResource : categoriesResource.getChildren()){
                dropdownTags.add(categoryResource.adaptTo(Tag.class));
            }
        }
    }
}
