package com.solidigm.core.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solidigm.core.services.BrightcoveVideoService;
import com.solidigm.core.services.BrightcoveVideoServiceConfig;
import com.solidigm.core.services.DynamicsArticlesServiceConfig;

@Component(service = BrightcoveVideoService.class, immediate = true)
@Designate(ocd = BrightcoveVideoServiceConfig.class)
public class BrightcoveVideoServiceConfigImpl {

    private String accountId;
    private String playerId;
    private String videoId;

    @Activate
    public void activate(BrightcoveVideoServiceConfig config) {
        accountId = config.getAccountId();
        playerId = config.getPlayerId();
        videoId = config.getVideoId();
    }

    public String getAccountId() {
        return accountId;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getPlayerId() {
        return playerId;
    }

}
