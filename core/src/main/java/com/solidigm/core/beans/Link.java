package com.solidigm.core.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Link {
    @Getter
    @Setter
    @Inject
    private String label;

    @Getter
    @Setter
    @Inject
    private String link;

    public String getTarget(){
        if (link.startsWith("/")){
            return "internal";
        } else {
            return "external";
        }
    };
}
