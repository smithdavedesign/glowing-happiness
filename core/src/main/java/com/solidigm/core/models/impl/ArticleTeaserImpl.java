package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.tagging.TagManager;
import com.solidigm.core.beans.ArticlePage;
import com.solidigm.core.models.ArticleTeaser;
import com.solidigm.core.utils.ImageUtil;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {ArticleTeaser.class, ComponentExporter.class},
        resourceType = ArticleTeaserImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class ArticleTeaserImpl implements ArticleTeaser {
    static final String RESOURCE_TYPE = "solidigm/components/articleteaser";

    @SlingObject
    SlingHttpServletRequest request;

    @ValueMapValue
    private String articlePagePath;

    private Boolean hasContentValue;

    @Getter
    private ArticlePage article;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @Override
    public Boolean hasContent() {
        return hasContentValue;
    }

    @PostConstruct
    private void initModel() {
        ResourceResolver resourceResolver = request.getResourceResolver();

        if (!StringUtils.isEmpty(articlePagePath)) {
            //Get page properties of the article page
            article = null;
            Resource articleResource = resourceResolver.getResource(articlePagePath.concat("/jcr:content"));
            if (articleResource != null) {
                article = articleResource.adaptTo(ArticlePage.class);
            }
            if (article != null) {
                //Set the article page path
                article.setPath(articlePagePath);

                //Set the tag title for the teaser
                TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
                if (tagManager != null) {
                    article.setTeaserCategory(tagManager.resolve(article.getArticleCategory()).getTitle());
                }

                //If no image alt was authored find the alt text from the dam
                if (StringUtils.isEmpty(article.getTeaserImageAltText())
                        && !StringUtils.isEmpty(article.getTeaserImage())) {
                    article.setTeaserImageAltText(
                            ImageUtil.getAltTextFromDAM(resourceResolver, article.getTeaserImage())
                    );
                }
            }
        }

        hasContentValue = article != null && StringUtils.isNotEmpty(article.getTeaserImage());
    }
}
