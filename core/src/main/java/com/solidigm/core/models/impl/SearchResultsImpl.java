package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.solidigm.core.beans.Filter;
import com.solidigm.core.models.SearchResults;
import com.solidigm.core.utils.SolidigmUtils;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Model(
  adaptables = {Resource.class, SlingHttpServletRequest.class},
  adapters = {SearchResults.class, ComponentExporter.class},
  resourceType = SearchResultsImpl.RESOURCE_TYPE,
  defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
  name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
  extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class SearchResultsImpl implements SearchResults {
    static final String RESOURCE_TYPE = "solidigm/components/searchresults";

    @SlingObject
    SlingHttpServletRequest request;

    @Inject
    @Source("script-bindings")
    Page currentPage;

    @OSGiService
    QueryBuilder builder;

    private Session session;

    @Inject
    @ChildResource
    @Getter
    private List<Filter> filters;

    @Getter
    private String rootPath;

    @Getter
    private String query;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @PostConstruct
    private void initModel() {
        //Set localized root path
        if(currentPage != null){
            rootPath = SolidigmUtils.getSiteRoot(currentPage.getPath());
        }

        //Get query param if provided
        RequestParameter queryParameter = request.getRequestParameterMap().getValue("query");
        if (queryParameter != null) {
            query = queryParameter.toString();
            //iterate through filters and return counts of hits in each filter
            ResourceResolver resourceResolver = request.getResourceResolver();
            session = resourceResolver.adaptTo(Session.class);
            Map<String, String> map = new HashMap<String, String>();
            if(filters != null) {
                for (Filter filter : filters) {
                    map.put("path", filter.getFilterPath());
                    map.put("type", "cq:Page");
                    map.put("fulltext", query);
                    map.put("group.p.or", "true");
                    map.put("group.1_property", "jcr:content/hideInNav");
                    map.put("group.1_property.operation", "unequals");
                    map.put("group.1_property.value", "true");
                    map.put("group.2_property", "jcr:content/hideInNav");
                    map.put("group.2_property.operation", "exists");
                    map.put("group.2_property.value", "false");
                    map.put("p.limit", "-1");
                    map.put("p.offset", "0");
                    SearchResult result = builder.createQuery(PredicateGroup.create(map), session).getResult();
                    filter.setCount(Math.toIntExact(result.getTotalMatches()));
                }
            }
        }

    }
}
