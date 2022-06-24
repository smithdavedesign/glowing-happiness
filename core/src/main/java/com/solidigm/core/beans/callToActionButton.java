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
public class callToActionButton {
    private static final String EMPTY_SELECTION_BUTTON_STYLE = "opaque";

    @Getter
    @Inject
    private String buttonLabel;

    @Getter
    @Inject
    private String buttonLink;

    @Inject
    @Named("buttonStyle")
    private String buttonStyleSelection;

    @Getter
    private String buttonStyle;

    @PostConstruct
    private void initModel() {
        buttonStyle = StringUtils.isEmpty(buttonStyleSelection) ? EMPTY_SELECTION_BUTTON_STYLE : buttonStyleSelection;
    }
}
