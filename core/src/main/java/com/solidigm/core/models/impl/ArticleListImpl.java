package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.tagging.TagManager;
import com.solidigm.core.beans.ArticlePage;
import com.solidigm.core.models.ArticleList;
import com.solidigm.core.utils.ImageUtil;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {ArticleList.class, ComponentExporter.class},
        resourceType = ArticleListImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class ArticleListImpl implements ArticleList {
    static final String RESOURCE_TYPE = "solidigm/components/articlelist";

    @SlingObject
    SlingHttpServletRequest request;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @Getter
    @ValueMapValue
    private String title;

    @Getter
    @ValueMapValue
    private String linkText;

    @Getter
    @ValueMapValue
    private String link;

    @Getter
    private List<ArticlePage> articles;

    private Boolean hasContentValue;

    @Override
    public Boolean hasContent() {
        return hasContentValue;
    }

    @PostConstruct
    private void initModel() {
        //initialize variables
        articles = new ArrayList<>();
        hasContentValue = false;
        ResourceResolver resourceResolver = request.getResourceResolver();
        Resource articlesResource = request.getResource().getChild("articles");

        //If articles have been authored, process the pages' data to be displayed as teasers
        if(articlesResource != null) {

            //Loop through the authored article pages
            for (Resource articleResource : articlesResource.getChildren()) {
                //Get article page path, if it is empty skip to next article
                String pagePath = articleResource.getValueMap().get("article", String.class);
                if (!StringUtils.isEmpty(pagePath)) {
                    //Get page properties of the article page, if it does not exist skip to the next article
                    ArticlePage articlePage =
                            resourceResolver.getResource(pagePath.concat("/jcr:content")).adaptTo(ArticlePage.class);
                    if (articlePage != null) {
                        //Set the article page path
                        articlePage.setPath(pagePath);

                        //Set the tag title for the teaser
                        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
                        articlePage.setTeaserCategory(tagManager.resolve(articlePage.getArticleCategory()).getTitle());

                        //If no image alt was authored find the alt text from the dam
                        if (StringUtils.isEmpty(articlePage.getTeaserImageAltText()) &&
                                !StringUtils.isEmpty(articlePage.getTeaserImage())) {
                            articlePage.setTeaserImageAltText(
                                    ImageUtil.getAltTextFromDAM(resourceResolver, articlePage.getTeaserImage())
                            );
                        }
                        articles.add(articlePage);
                    }
                }
            }

            hasContentValue = !StringUtils.isEmpty(title) && !articles.isEmpty();
        }
    }
}
