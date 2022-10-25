package com.solidigm.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
  name = "Solidigm Brightcove Video Configuration",
  description = "Configure various properties used for Brightcove Video Embed in components"
)
public @interface BrightcoveVideoServiceConfig {
    @AttributeDefinition(
      name = "Account ID",
      description = "Brightcove publisher ID")
    String getAccountId();

    @AttributeDefinition(
      name = "Player ID",
      description = "ID for video player")
    String getPlayerId();

    @AttributeDefinition(
      name = "Video ID",
      description = "ID for default video to play if component author does not specify")
    String getVideoId();



}
