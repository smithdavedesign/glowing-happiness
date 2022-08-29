package com.solidigm.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Solidigm Social Sharing Configuration",
        description = "Configures the base URL for sharing pages on social media sites"
)
public @interface SocialConfig {
    @AttributeDefinition(
            name = "Twitter URL",
            description = "Base URL for sharing pages to Twitter")
    String getTwitterUrl() default "https://twitter.com/intent/tweet?text=";

    @AttributeDefinition (
            name = "LinkedIn URL",
            description = "Base URL for sharing pages to LinkedIn")
    String getLinkedInUrl() default "https://linkedin.com/shareArticle?url=";
}
