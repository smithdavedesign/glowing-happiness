package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.wcm.api.Page;
import com.solidigm.core.beans.ProductSeriesPage;
import com.solidigm.core.models.SeriesList;
import com.solidigm.core.utils.SolidigmConstants;
import lombok.Getter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {SeriesList.class, ComponentExporter.class},
        resourceType = SeriesListImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class SeriesListImpl implements SeriesList {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    static final String RESOURCE_TYPE = "solidigm/components/serieslist";

    @SlingObject
    SlingHttpServletRequest request;

    @Inject
    @Source("script-bindings")
    Page currentPage;

    @OSGiService
    private QueryBuilder queryBuilder;

    @Getter
    private List<ProductSeriesPage> seriesPages;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @PostConstruct
    private void initModel() {
        if (currentPage != null) {
            //Execute query to get all immediate child pages
            //TODO: this should only grab series pages, update with authoring on site has been corrected
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put("path", currentPage.getPath());
            queryMap.put("path.flat", "true");
            queryMap.put("type", "cq:Page");
            Session session = request.getResourceResolver().adaptTo(Session.class);
            Query query = queryBuilder.createQuery(PredicateGroup.create(queryMap), session);
            List<Hit> hits = query.getResult().getHits();

            //Populate seriesPages list with ProductSeriesPages from the query results
            seriesPages = new ArrayList<>();
            for (Hit hit : hits) {
                try {
                    seriesPages.add(new ProductSeriesPage(request.getResourceResolver(), hit.getPath()));
                } catch (RepositoryException e) {
                    log.error("Repository exception on series page query", e);
                }
            }
        }
    }
}
