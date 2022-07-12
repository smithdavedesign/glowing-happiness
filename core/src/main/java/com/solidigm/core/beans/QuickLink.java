package com.solidigm.core.beans;

import org.apache.commons.lang.StringUtils;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class QuickLink extends Link{

    static final String EXTERNAL_STYLE_CLASS = "cmp-quick-links__link--external";
    static final String TARGET_SELF = "_self";

    @Getter
    @Inject
    private String linkTitle;

    @Getter
    @Inject
    private String linkSubTitle;

    private String linkType;

    public String getLinkType() {
        if (StringUtils.equals(getTarget(), TARGET_SELF)){
            linkType = "";
        } else {
            linkType = EXTERNAL_STYLE_CLASS;
        }
        return linkType;
    }
}
