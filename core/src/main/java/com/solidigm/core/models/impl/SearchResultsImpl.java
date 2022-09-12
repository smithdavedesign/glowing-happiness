package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.solidigm.core.beans.Filter;
import com.solidigm.core.beans.SearchResult;
import com.solidigm.core.models.SearchResults;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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

    @ChildResource
    @Getter
    private List<Filter> filters;

    @Getter
    private List<SearchResult> searchResults;

    @Getter
    private String resultsPerPage;

    @Getter
    private String totalResults;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @PostConstruct
    private void initModel() {
         searchResults = new ArrayList<>();
         for(int i = 0; i < 40; i++) {
             searchResults.add(
                     new SearchResult("/content/solidigm/language-masters/en", "Test Title", "Test Description",
                                      "https://google.com",
                                      "/content/dam/solidigm/master/products/d3/s4520/d3-s4520.png", "Test Alt Text"));
         }
         resultsPerPage = "10";
         totalResults = "40";
    }
}
