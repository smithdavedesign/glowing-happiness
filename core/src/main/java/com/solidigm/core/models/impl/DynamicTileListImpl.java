package com.solidigm.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.solidigm.core.beans.TileData;
import com.solidigm.core.models.DynamicTileList;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = {DynamicTileList.class, ComponentExporter.class},
        resourceType = com.solidigm.core.models.impl.DynamicTileListImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter( //Exporter annotation that serializes the model as JSON
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class DynamicTileListImpl implements DynamicTileList {
    static final String RESOURCE_TYPE = "solidigm/components/dynamictilelist";

    @Inject
    Resource resource;

    @ChildResource
    private List<TileData> tileInfo;

    private Boolean hasContentVal;

    @Getter
    private String path;

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @Override
    public Boolean hasContent() {
        return hasContentVal;
    }

    @PostConstruct
    private void initModel() {
        if (resource != null) {
            path = resource.getPath();
        }

        hasContentVal = tileInfo != null && !tileInfo.isEmpty();
    }
}
