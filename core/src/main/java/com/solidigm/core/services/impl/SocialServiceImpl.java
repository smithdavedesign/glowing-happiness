package com.solidigm.core.services.impl;

import com.solidigm.core.services.SocialConfig;
import com.solidigm.core.services.SocialService;
import lombok.Getter;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

@Component(immediate = true, service = SocialService.class)
@Designate(ocd = SocialConfig.class)
public class SocialServiceImpl implements SocialService {
    @Getter
    private String twitterShareUrl;

    @Getter
    private String linkedInShareUrl;

    @Activate
    public void activate(SocialConfig socialConfig) {
        twitterShareUrl = socialConfig.getTwitterUrl();
        linkedInShareUrl = socialConfig.getLinkedInUrl();
    }
}
