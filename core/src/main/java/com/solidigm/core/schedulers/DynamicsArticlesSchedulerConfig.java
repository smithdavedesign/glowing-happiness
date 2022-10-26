package com.solidigm.core.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Solidigm Dynamic Articles Scheduler Configuration",
        description = "Configure when to automatically run the Knowledge Article sync from Dynamics."
)
public @interface DynamicsArticlesSchedulerConfig {
    @AttributeDefinition(
            name = "Cron Schedule",
            description = "Cron schedule that determines when Knowledge Articles from Dynamics will sync with AEM automatically if enabled.")
    String getSchedulerExpression() default "0 20 * * *";

    @AttributeDefinition(
            name = "Enable Scheduler",
            description = "Enable the automatic sync of Knowledge Articles from Dynamics on the configured schedule.",
            type = AttributeType.BOOLEAN)
    boolean getEnableScheduler() default false;

    @AttributeDefinition(
            name = "Run sync on save",
            description = "Enable to run the sync of Knowledge Articles from Dynamics to AEM immediately on saving this configuration.",
            type = AttributeType.BOOLEAN)
    boolean getRunOnSave() default false;
}
