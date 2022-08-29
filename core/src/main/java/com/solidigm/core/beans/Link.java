package com.solidigm.core.beans;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Link {
    @Getter
    @Setter
    @Inject
    private String label;

    @Getter
    @Inject
    private String link;

    @Getter
    private String target;

    public void setLink(String link) {
        this.link = link;
        processLink();
    }

    @PostConstruct
    private void initModel() {
        processLink();
    }

    public Link() {

    }

    public Link(String label, String link) {
        this.label = label;
        this.link = link;
        processLink();
    }

    private void processLink() {
        if (!StringUtils.isEmpty(link) && link.startsWith("/")){
            target = "_self";
            if (StringUtils.isEmpty(FilenameUtils.getExtension(link))) {
                link = link.concat(".html");
            }
        } else {
            target = "_blank";
        }
    }
}
