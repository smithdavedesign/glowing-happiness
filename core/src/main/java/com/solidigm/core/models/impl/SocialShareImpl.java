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
import com.solidigm.core.models.SocialShare;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    static final String RESOURCE_TYPE = "solidigm/components/socialshare";

    static final String ARTICLE_TEMPLATE = "/conf/solidigm/settings/wcm/templates/article-page";

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
        Page currentPage = request.getResourceResolver().adaptTo(PageManager.class).getContainingPage(resource);

        //Stop processing if current page is null
        if (currentPage == null){
            return;
        }

        //Create the query params shared between the previous article and next article query
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", "/content/solidigm");
        queryMap.put("type", "cq:Page");
        queryMap.put("1_property", "jcr:content/cq:template");
        queryMap.put("1_property.operation", "equals");
        queryMap.put("1_property.value", ARTICLE_TEMPLATE);
        queryMap.put("2_property", "jcr:content/articleCategory");
        queryMap.put("2_property.operation", "equals");
        queryMap.put("2_property.value", currentPage.getProperties().get("articleCategory", String.class));
        queryMap.put("orderby", "@jcr:content/articleDate");
        queryMap.put("p.limit", "2");

        //Create the next article query params
        Map<String, String> nextMap = new HashMap<>();
        nextMap.put("orderby.sort", "asc");
        nextMap.put("daterange.property", "@jcr:content/articleDate");
        nextMap.put("daterange.lowerBound", currentPage.getProperties().get("articleDate", String.class));
        nextMap.put("daterange.lowerOperation", ">=");
        nextMap.putAll(queryMap);

        //Create the previous article query params
        Map<String, String> prevMap = new HashMap<>();
        prevMap.put("orderby.sort", "desc");
        prevMap.put("daterange.property", "@jcr:content/articleDate");
        prevMap.put("daterange.upperBound", currentPage.getProperties().get("articleDate", String.class));
        prevMap.put("daterange.upperOperation", "<=");
        prevMap.putAll(queryMap);

        //Execute the query for the previous and next article
        Session session = request.getResourceResolver().adaptTo(Session.class);
        Query nextQuery = queryBuilder.createQuery(PredicateGroup.create(nextMap), session);
        Query prevQuery = queryBuilder.createQuery(PredicateGroup.create(prevMap), session);
        SearchResult nextResult = nextQuery.getResult();
        SearchResult prevResult = prevQuery.getResult();
        List<Hit> nextHits = nextResult.getHits();
        List<Hit> prevHits = prevResult.getHits();

        //Get the path of the previous and next article, performing necessary logic to filter out the current page
        try {
            for (Hit hit : nextHits){
                if (!hit.getPath().equals(currentPage.getPath())){
                    next = hit.getPath();
                    break;
                }
            }
            for (Hit hit : prevHits){
                if (!hit.getPath().equals(currentPage.getPath())){
                    previous = hit.getPath();
                    break;
                }
            }
        } catch (RepositoryException e) {
            //On a repository error: log and catch
            log.error("Repository error on Article Page query in Social Share", e);
        }
    }
}
