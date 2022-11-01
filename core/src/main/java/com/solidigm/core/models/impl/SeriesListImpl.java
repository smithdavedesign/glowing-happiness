package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.Page;
import com.solidigm.core.beans.ProductSeriesPage;
import com.solidigm.core.models.SeriesList;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    @Getter
    private List<ProductSeriesPage> seriesPages;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @PostConstruct
    private void initModel() {
        //use sling to gather children of current page
        if (currentPage != null) {
            seriesPages = new ArrayList<>();
            Iterator<Page> children = currentPage.listChildren();
            while(children.hasNext()){
                Page child = children.next();
                seriesPages.add(new ProductSeriesPage(request.getResourceResolver(), child.getPath()));

            }
        }
    }
}
