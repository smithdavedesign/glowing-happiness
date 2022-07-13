package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.solidigm.core.models.Footer;
import com.solidigm.core.models.SocialShare;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {SocialShare.class, ComponentExporter.class},
        resourceType = SocialShareImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class SocialShareImpl implements SocialShare {

    static final String RESOURCE_TYPE = "solidigm/components/socialshare";

    @SlingObject
    SlingHttpServletRequest request;

    @Inject
    private Resource resource;

    @OSGiService
    private QueryBuilder queryBuilder;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @Getter
    private String next;

    @Getter
    private String previous;

    @PostConstruct
    private void initModel() {
        next = "#test";
        previous = "#test";
        Page currentPage = request.getResourceResolver().adaptTo(PageManager.class).getContainingPage(resource);

        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", "/content/solidigm");
        queryMap.put("type", "cq:Page");

        Session session = request.getResourceResolver().adaptTo(Session.class);
        Query query = queryBuilder.createQuery(PredicateGroup.create(queryMap), session);
        SearchResult result = query.getResult();
        List<Hit> hits = result.getHits();
        try {
            next = hits.get(0).getPath();
            previous = hits.get(1).getPath();
        } catch (RepositoryException e) {

        }
    }
}
