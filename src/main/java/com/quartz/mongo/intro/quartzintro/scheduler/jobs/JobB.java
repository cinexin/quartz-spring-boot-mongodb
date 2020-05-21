package com.quartz.mongo.intro.quartzintro.scheduler.jobs;

import com.quartz.mongo.intro.quartzintro.config.QuartzConfiguration;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class JobB extends QuartzJobBean {

    private static Logger log = LoggerFactory.getLogger(JobB.class);

    private ApplicationContext applicationContext;
    /**
     * This method is called by Spring since we set the
     * {@link SchedulerFactoryBean#setApplicationContextSchedulerContextKey(String)}
     * in {@link QuartzConfiguration}
     *
     * @param applicationContext
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("This is the B job, executed by {}", applicationContext.getBean(Environment.class));
    }
}
