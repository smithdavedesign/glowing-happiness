package com.solidigm.core.beans;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TileData extends Link{
    @Getter
    @ValueMapValue
    private String icon;

    @Getter
    @ValueMapValue
    private String title;

    @Getter
    @ValueMapValue
    private String description;
}
