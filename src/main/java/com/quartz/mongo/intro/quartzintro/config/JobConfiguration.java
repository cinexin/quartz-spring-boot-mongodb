package com.quartz.mongo.intro.quartzintro.config;

import com.quartz.mongo.intro.quartzintro.constants.SchedulerConstants;
import com.quartz.mongo.intro.quartzintro.scheduler.jobs.JobA;
import com.quartz.mongo.intro.quartzintro.scheduler.jobs.JobB;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 
 * This will configure the job to run within quartz.
 * 
 * @author dinuka
 *
 */
@Configuration
public class JobConfiguration {

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@PostConstruct
	private void initialize() throws Exception {
		schedulerFactoryBean.getScheduler().addJob(jobADetail(), true, true);
		schedulerFactoryBean.getScheduler().addJob(jobBDetail(), true, true);
		if (
				!schedulerFactoryBean.getScheduler().checkExists(
						new TriggerKey(SchedulerConstants.POLLING_TRIGGER_KEY_A, SchedulerConstants.POLLING_GROUP_A)
				)
				&& !schedulerFactoryBean.getScheduler().checkExists(
						new TriggerKey(SchedulerConstants.POLLING_JOB_KEY_B, SchedulerConstants.POLLING_GROUP_B)
				)
		) {
			schedulerFactoryBean.getScheduler().scheduleJob(jobATrigger());
			schedulerFactoryBean.getScheduler().scheduleJob(jobBTrigger());
		}

	}

	/**
	 * <p>
	 * The job is configured here where we provide the job class to be run on
	 * each invocation. We give the job a name and a value so that we can
	 * provide the trigger to it on our method {@link #jobATrigger()}
	 * </p>
	 * 
	 * @return an instance of {@link JobDetail}
	 */
	private static JobDetail jobADetail() {
		JobDetailImpl jobDetail = new JobDetailImpl();
		jobDetail.setKey(new JobKey(SchedulerConstants.POLLING_JOB_KEY_A, SchedulerConstants.POLLING_GROUP_A));
		jobDetail.setJobClass(JobA.class);
		jobDetail.setDurability(true);
		return jobDetail;
	}

	/**
	 * <p>
	 * The job is configured here where we provide the job class to be run on
	 * each invocation. We give the job a name and a value so that we can
	 * provide the trigger to it on our method {@link #jobATrigger()}
	 * </p>
	 *
	 * @return an instance of {@link JobDetail}
	 */
	private static JobDetail jobBDetail() {
		JobDetailImpl jobDetail = new JobDetailImpl();
		jobDetail.setKey(new JobKey(SchedulerConstants.POLLING_JOB_KEY_A, SchedulerConstants.POLLING_GROUP_B));
		jobDetail.setJobClass(JobB.class);
		jobDetail.setDurability(true);
		return jobDetail;
	}
	/**
	 * <p>
	 * This method will define the frequency with which we will be running the
	 * scheduled job which in this instance is every minute three seconds after
	 * the start up.
	 * </p>
	 * 
	 * @return an instance of {@link Trigger}
	 */
	private static Trigger jobATrigger() {
		return newTrigger().forJob(jobADetail())
				.withIdentity(SchedulerConstants.POLLING_TRIGGER_KEY_A, SchedulerConstants.POLLING_GROUP_A)
				.withPriority(50).withSchedule(SimpleScheduleBuilder.repeatMinutelyForever())
				.startAt(Date.from(LocalDateTime.now().plusSeconds(3).atZone(ZoneId.systemDefault()).toInstant()))
				.build();
	}

	/**
	 * <p>
	 * This method will define the frequency with which we will be running the
	 * scheduled job which in this instance is every minute three seconds after
	 * the start up.
	 * </p>
	 *
	 * @return an instance of {@link Trigger}
	 */
	private static Trigger jobBTrigger() {
		return newTrigger().forJob(jobBDetail())
				.withIdentity(SchedulerConstants.POLLING_TRIGGER_KEY_B, SchedulerConstants.POLLING_GROUP_B)
				.withPriority(50).withSchedule(SimpleScheduleBuilder.repeatSecondlyForever())
				.startAt(Date.from(LocalDateTime.now().plusSeconds(3).atZone(ZoneId.systemDefault()).toInstant()))
				.build();
	}
}