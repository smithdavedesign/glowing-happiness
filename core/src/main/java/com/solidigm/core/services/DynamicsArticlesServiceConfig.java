package com.solidigm.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Solidigm Dynamic Articles Service Configuration",
        description = "Configure various properties used to import Knowledge Articles from dynamics into AEM"
)
public @interface DynamicsArticlesServiceConfig {
    @AttributeDefinition(
            name = "Support Page Path",
            description = "Path to the support page in AEM within the US English language master that contains support index pages (e.g., Performance) that Knowledge Articles from Dynamics are created under.")
    String getSupportPagePath() default "/content/solidigm/language-masters/en/support-page";

    @AttributeDefinition(
            name = "Support Index Pages",
            description = "List the names of the support index pages separated by a comma from their respective Article Content Type Code configured in Dynamics. E.g. 'support-index-performance,971470001'.")
    String[] getSupportIndexPages();

    @AttributeDefinition(
            name = "Token Authority",
            description = "URL of the token authority for Dynamics.")
    String getAuthority();

    @AttributeDefinition(
            name = "Client ID",
            description = "Client ID for the Dynamics API")
    String getClientId();

    @AttributeDefinition(
            name = "Client Secret",
            description = "Client Secret for the Dynamics API",
            type = AttributeType.PASSWORD)
    String getClientSecret();

    @AttributeDefinition(
            name = "Dynamics API URL",
            description = "URL for the Dynamics API that returns the Knowledge Articles to sync with AEM")
    String getApiUrl();

    @AttributeDefinition(
            name = "Run sync on save",
            description = "Enable to run the sync of Knowledge Articles from Dynamics to AEM immediately on saving this configuration.",
            type = AttributeType.BOOLEAN)
    boolean getRunOnSave() default false;
}
