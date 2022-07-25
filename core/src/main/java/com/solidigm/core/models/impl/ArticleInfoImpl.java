package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.solidigm.core.beans.ArticlePage;
import com.solidigm.core.beans.Link;
import com.solidigm.core.models.ArticleInfo;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {ArticleInfo.class, ComponentExporter.class},
        resourceType = ArticleInfoImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class ArticleInfoImpl implements ArticleInfo{
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    static final String RESOURCE_TYPE = "solidigm/components/articleinfo";

    @SlingObject
    SlingHttpServletRequest request;

    @Inject
    @Source("script-bindings")
    Page currentPage;

    @ValueMapValue
    private String taggedArticlesPage;

    @Getter
    private String date;

    @Getter
    private String author;

    @Getter
    private List<Link> tags;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @PostConstruct
    private void initModel() {
        try {
            //Init variables
            TagManager tagManager = request.getResourceResolver().adaptTo(TagManager.class);
            ArticlePage articlePage = currentPage.getContentResource().adaptTo(ArticlePage.class);

            //Set author
            author = articlePage.getArticleAuthor();

            //Set date
            SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy");
            date = formatter.format(articlePage.getArticleDate());

            //Set tags
            if(StringUtils.isNotEmpty(taggedArticlesPage)) {
                tags = new ArrayList<>();
                for (String tag : articlePage.getArticleTags()) {
                    Link link = new Link();
                    link.setLink(
                        taggedArticlesPage.concat(".")
                        .concat(URLEncoder.encode(tag, StandardCharsets.UTF_8))
                        .concat(".html")
                    );
                    link.setLabel(tagManager.resolve(tag).getTitle());
                    tags.add(link);
                }
            }
        } catch (NullPointerException e) {
            log.error("Error generating article info", e);
        }
    }
}
