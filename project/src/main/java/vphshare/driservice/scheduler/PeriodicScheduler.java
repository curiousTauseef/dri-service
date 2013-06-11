package vphshare.driservice.scheduler;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Calendar;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;

import vphshare.driservice.config.Configuration;

public class PeriodicScheduler {
	private static final Logger logger = Logger.getLogger(PeriodicScheduler.class);
	
	private final Scheduler scheduler;
	
	@Inject
	public PeriodicScheduler(SchedulerFactory factory, InjectingJobFactory jobFactory, Configuration cfg) {
		Scheduler scheduler = null;
		try  {
			logger.info("Started configuring periodic scheduler!");
			scheduler = factory.getScheduler();
			scheduler.setJobFactory(jobFactory);
			
			logger.info("Configuring periodic validation job for all supervised directories");
			JobDetail job = newJob(AllDirectoriesValidationJob.class)
					.withIdentity("periodic-validation-job", "validation")
					.build();
	
			logger.info("Configuring periodic trigger for periodic job");
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis() + cfg.getValidationStartDelay() * 1000L);
			Trigger trigger = newTrigger()
					.withIdentity("periodic-validation-runner")
					.withSchedule(simpleSchedule()
							.withIntervalInMinutes(cfg.getValidationPeriod())
							.repeatForever())
					.startNow()
					.build();
	
			logger.info("Scheduling periodic validation job with trigger [validationPeriod=" + cfg.getValidationPeriod() + "min]");
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
			logger.info("Finished configuring periodic scheduler!");
			
		} catch (SchedulerException e) {
			logger.error("Unable to create scheduler", e);
	
		} finally {
			this.scheduler = scheduler;
		}
	}
	
	public Scheduler getScheduler() {
		return scheduler;
	}
	
	public void start() {
		try {
			scheduler.start();
			logger.info("Scheduler started successfully!");
		} catch (SchedulerException e) {
			logger.error("Unable to start the scheduler", e);
		}
	}
	
	public void shutdown() {
		try {
			scheduler.shutdown(false);
			logger.info("Scheduler stopped successfully!");
		} catch (SchedulerException e) {
			logger.error("Unable to shutdown the scheduler", e);
		}
	}
}
