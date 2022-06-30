package com.solidigm.core.beans;

import org.apache.commons.lang.StringUtils;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class QuickLinkTile {

    static final String EXTERNAL_STYLE_CLASS = "cmp-quick-links__link--external";

    @Getter
    @Inject
    private String tileTitle;

    @Getter
    @Inject
    private String tileSubTitle;

    @Getter
    @Inject
    private String tileLink;

    @Getter
    private String target;

    @Getter
    private String linkType;


    @PostConstruct
    private void initModel() {
        processLink();
    }


    private void processLink() {
        if (!StringUtils.isEmpty(tileLink) && tileLink.startsWith("/")){
            target = "_self";
            linkType = "";
            if (StringUtils.isEmpty(FilenameUtils.getExtension(tileLink))) {
                tileLink = tileLink.concat(".html");
            }
        } else {
            target = "_blank";
            linkType = EXTERNAL_STYLE_CLASS;
        }
    }
}
