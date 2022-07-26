package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.solidigm.core.models.ArticleTeaser;
import com.solidigm.core.models.TaggedArticles;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    @Getter
    private String selectorTag;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @PostConstruct
    private void initModel() {
        String[] selectors = request.getRequestPathInfo().getSelectors();
        if(selectors.length > 0) {
            selectorTag = URLDecoder.decode(selectors[0], StandardCharsets.UTF_8);
        } else {
            selectorTag = "";
        }
    }
}
