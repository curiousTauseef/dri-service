package vphshare.driservice.scheduler;

import javax.inject.Inject;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import com.google.inject.Injector;

public class InjectingJobFactory implements JobFactory {


	private final Injector injector;

	@Inject
	public InjectingJobFactory(final Injector injector) {
		this.injector = injector;
	}
	
	@Override
	public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) {
		JobDetail jobDetail = bundle.getJobDetail();
		Job job = (Job) injector.getInstance(jobDetail.getJobClass());
		injector.injectMembers(job);
		return job;
	}
}
