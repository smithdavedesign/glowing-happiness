package com.solidigm.core.beans;

import com.solidigm.core.utils.LinkUtil;
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
        this.link = LinkUtil.processLink(link);
        this.target = LinkUtil.processTarget(link);

    }

    @PostConstruct
    private void initModel() {
        link = LinkUtil.processLink(link);
        target = LinkUtil.processTarget(link);
    }
}
