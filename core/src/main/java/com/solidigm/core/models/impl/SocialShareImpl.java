package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.wcm.api.Page;
import com.solidigm.core.models.SocialShare;
import com.solidigm.core.services.SocialService;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    static final String RESOURCE_TYPE = "solidigm/components/socialshare";

    static final String ARTICLE_TEMPLATE = "/conf/solidigm/settings/wcm/templates/article-page";

    static final String EVENT_TEMPLATE = "/conf/solidigm/settings/wcm/templates/event-page";

    static final int HOME_NAVIGATION_LEVEL = 5;

    @SlingObject
    SlingHttpServletRequest request;

    @Inject
    @Source("script-bindings")
    Page currentPage;

    @OSGiService
    SocialService socialService;

    @Inject
    private Resource resource;

    @OSGiService
    private QueryBuilder queryBuilder;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @Getter
    private String twitterShare;

    @Getter
    private String linkedInShare;

    @Getter
    private String next;

    @Getter
    private String previous;

    @PostConstruct
    private void initModel() {
        //Process sharing links
        String url = request.getRequestURL().toString();
        url = URLEncoder.encode(url, StandardCharsets.UTF_8);
        twitterShare = socialService.getTwitterShareUrl().concat(url);
        linkedInShare = socialService.getLinkedInShareUrl().concat(url);

        //Stop processing if current page is null
        if (currentPage == null){
            return;
        }

        //Get localized root path
        String rootPath = currentPage.getPath();
        int endIndex = StringUtils.ordinalIndexOf(rootPath, "/", HOME_NAVIGATION_LEVEL);
        rootPath = rootPath.substring(0, endIndex);

        //Get current category
        String currentCategory = currentPage.getProperties().get("articleCategory", String.class);
        if (StringUtils.isEmpty(currentCategory)) {
            currentCategory = currentPage.getProperties().get("eventCategory", String.class);
        }

        //Create the query params
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", rootPath);
        queryMap.put("type", "cq:Page");
        queryMap.put("group.p.or", "true");
        queryMap.put("group.1_group.1_property", "jcr:content/cq:template");
        queryMap.put("group.1_group.1_property.operation", "equals");
        queryMap.put("group.1_group.1_property.value", ARTICLE_TEMPLATE);
        queryMap.put("group.1_group.2_property", "jcr:content/articleCategory");
        queryMap.put("group.1_group.2_property.operation", "equals");
        queryMap.put("group.1_group.2_property.value", currentCategory);
        queryMap.put("group.2_group.1_property", "jcr:content/cq:template");
        queryMap.put("group.2_group.1_property.operation", "equals");
        queryMap.put("group.2_group.1_property.value", EVENT_TEMPLATE);
        queryMap.put("group.2_group.2_property", "jcr:content/eventCategory");
        queryMap.put("group.2_group.2_property.operation", "equals");
        queryMap.put("group.2_group.2_property.value", currentCategory);
        queryMap.put("orderby", "@jcr:content/date");
        queryMap.put("orderby.sort", "desc");
        queryMap.put("p.limit", "-1");

        //Execute the query for the articles and events sorted by date
        Session session = request.getResourceResolver().adaptTo(Session.class);
        Query query = queryBuilder.createQuery(PredicateGroup.create(queryMap), session);
        List<Hit> hits = query.getResult().getHits();

        //Get the path of the previous and next page, by finding the current page in the ordered results
        try {
            for (int i = 0; i < hits.size(); i++) {
                Hit hit = hits.get(i);
                if (hit.getPath().equals(currentPage.getPath())) {
                    if(i > 0) {
                        next = hits.get(i - 1).getPath();
                    }
                    if((i + 1) < hits.size()) {
                        previous = hits.get(i + 1).getPath();
                    }
                }
            }
        } catch (RepositoryException e) {
            //On a repository error: log and catch
            log.error("Repository error on query in Social Share", e);
        }
    }
}
