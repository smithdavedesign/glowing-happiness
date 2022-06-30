package com.solidigm.core.beans;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CallToActionButton extends Link{
    private static final String EMPTY_SELECTION_BUTTON_STYLE = "opaque";

    @Inject
    @Named("buttonStyle")
    private String buttonStyleSelection;

    @Getter
    private String buttonStyle;

    @PostConstruct
    private void init() {
        buttonStyle = StringUtils.isEmpty(buttonStyleSelection) ? EMPTY_SELECTION_BUTTON_STYLE : buttonStyleSelection;
    }
}
