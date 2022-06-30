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


    @PostConstruct
    private void initModel() {
        processLink();
    }


    private void processLink() {
        if (!StringUtils.isEmpty(tileLink) && tileLink.startsWith("/")){
            target = "_self";
            if (StringUtils.isEmpty(FilenameUtils.getExtension(tileLink))) {
                tileLink = tileLink.concat(".html");
            }
        } else {
            target = "_blank";
        }
    }
}
