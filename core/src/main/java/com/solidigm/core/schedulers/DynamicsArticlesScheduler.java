package com.solidigm.core.schedulers;


import com.solidigm.core.services.DynamicsArticlesService;
import com.solidigm.core.services.impl.DynamicsArticlesServiceImpl;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Designate(ocd = DynamicsArticlesSchedulerConfig.class)
@Component(service = DynamicsArticlesScheduler.class, immediate = true)
public class DynamicsArticlesScheduler implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(DynamicsArticlesServiceImpl.class);

    @Reference
    private DynamicsArticlesService dynamicsArticlesService;

    @Reference
    private Scheduler scheduler;

    private String schedule;

    String id = String.valueOf(this.getClass().hashCode());

    private void addScheduler() {
        ScheduleOptions scheduleOptions = scheduler.EXPR(schedule);
        scheduleOptions.name(id);
        scheduleOptions.canRunConcurrently(false);
        scheduler.schedule(this, scheduleOptions);
    }

    @Activate
    public void activate(DynamicsArticlesSchedulerConfig config) {
        schedule = config.getSchedulerExpression();
        if (config.getEnableScheduler()) {
            addScheduler();
        }
        if (config.getRunOnSave()) {
            run();
        }
    }

    @Modified
    public void modified(DynamicsArticlesSchedulerConfig config) {
        schedule = config.getSchedulerExpression();
        scheduler.unschedule(id);
        if (config.getEnableScheduler()) {
            addScheduler();
        }
        if (config.getRunOnSave()) {
            run();
        }
    }

    @Override
    public void run() {
        logger.info("Running Knowledge Articles sync from dynamics");
        String accessToken = dynamicsArticlesService.acquireToken();
        String articleJson = dynamicsArticlesService.fetchArticles(accessToken);
        dynamicsArticlesService.processArticles(articleJson);
    }
}
