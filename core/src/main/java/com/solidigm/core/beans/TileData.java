package com.solidigm.core.beans;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TileData extends Link{
    static final String EXTERNAL_STYLE_CLASS = "solidigm-btn--external";
    static final String TARGET_SELF = "_self";
    @Getter
    @ValueMapValue
    private String icon;

    @Getter
    @ValueMapValue
    private String title;

    @Getter
    @ValueMapValue
    private String description;

    @Getter
    private String linkType;

    public String getLinkType() {
        return (StringUtils.equals(getTarget(), TARGET_SELF) ? "" : EXTERNAL_STYLE_CLASS);
    }
}
