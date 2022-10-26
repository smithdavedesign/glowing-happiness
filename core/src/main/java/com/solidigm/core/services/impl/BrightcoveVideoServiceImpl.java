package com.solidigm.core.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import com.solidigm.core.services.BrightcoveVideoService;
import com.solidigm.core.services.BrightcoveVideoServiceConfig;

@Component(service = BrightcoveVideoService.class, immediate = true)
@Designate(ocd = BrightcoveVideoServiceConfig.class)
public class BrightcoveVideoServiceImpl implements BrightcoveVideoService {

    private String accountId;
    private String playerId;

    @Activate
    public void activate(BrightcoveVideoServiceConfig config) {
        accountId = config.getAccountId();
        playerId = config.getPlayerId();
    }

    public String getAccountId() {
        return accountId;
    }

    public String getPlayerId() {
        return playerId;
    }

}
