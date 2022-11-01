package com.solidigm.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
  name = "Solidigm Brightcove Video Configuration",
  description = "Configure various properties used for Brightcove Video Embed in components"
)
public @interface BrightcoveVideoServiceConfig {
    @AttributeDefinition(
      name = "Account ID",
      description = "Brightcove publisher ID",
      type = AttributeType.STRING)
    String getAccountId() default "6272332767001";

    @AttributeDefinition(
      name = "Player ID",
      description = "ID for video player",
      type = AttributeType.STRING)
    String getPlayerId() default "5ukNGR047";




}
