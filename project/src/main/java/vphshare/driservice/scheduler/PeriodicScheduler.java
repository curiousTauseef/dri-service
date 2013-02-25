package vphshare.driservice.scheduler;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Calendar;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;

import vphshare.driservice.config.Configuration;

public class PeriodicScheduler {
	private static final Logger LOG = Logger.getLogger(PeriodicScheduler.class.getName());
	
	private final Scheduler scheduler;
	
	@Inject
	public PeriodicScheduler(SchedulerFactory factory, InjectingJobFactory jobFactory, Configuration cfg) {
		Scheduler scheduler = null;
		try  {
			LOG.log(INFO, "Started configuring periodic scheduler!");
			scheduler = factory.getScheduler();
			scheduler.setJobFactory(jobFactory);
			
			JobDetail job = newJob(AllDatasetsValidationJob.class)
					.withIdentity("periodic-validation-job", "validation")
					.build();
	
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis() + cfg.getValidationStartDelay() * 1000L);
			Trigger trigger = newTrigger()
					.withIdentity("periodic-validation-runner")
					.withSchedule(simpleSchedule()
							.withIntervalInMinutes(cfg.getValidationPeriod())
							.repeatForever())
					.startNow()
					.build();
	
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
			LOG.log(INFO, "Finished configuring periodic scheduler!");
			
		} catch (SchedulerException e) {
			LOG.log(SEVERE, "Unable to create scheduler", e);
	
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
			LOG.log(INFO, "Scheduler started successfully!");
		} catch (SchedulerException e) {
			LOG.log(SEVERE, "Unable to start the scheduler", e);
		}
	}
	
	public void shutdown() {
		try {
			scheduler.shutdown(false);
			LOG.log(INFO, "Scheduler stopped successfully!");
		} catch (SchedulerException e) {
			LOG.log(SEVERE, "Unable to shutdown the scheduler", e);
		}
	}
}
