package vphshare.driservice.scheduler;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.validation.ComputeChecksumsJob;
import vphshare.driservice.validation.DirectoryValidationJob;
import vphshare.driservice.validation.UpdateSingleItemChecksumJob;

public class QuartzTaskSubmitter implements TaskSubmitter {

	private static final Logger logger = Logger.getLogger(QuartzTaskSubmitter.class);
	
	private Scheduler scheduler;

	@Inject
	public QuartzTaskSubmitter(PeriodicScheduler periodicScheduler) {
		scheduler = periodicScheduler.getScheduler();
	}
	
	@Override
	public void submitValidationJob(CloudDirectory directory) throws SchedulerException {
		synchronized (this.getClass()) {
			try {
				JobDetail job = newJob(DirectoryValidationJob.class)
						.withIdentity(directory.getName(), "dataset-validation-job")
						.build();
				job.getJobDataMap().put("dataset", directory);
	
				Trigger trigger = newTrigger()
						.withIdentity(directory.getName(), "dataset-validation-trigger")
						.startNow()
						.build();
				
				logger.info("Scheduling job: " + job.getKey() + ", " + job.getDescription());
				scheduler.scheduleJob(job, trigger);
				
			} catch (ObjectAlreadyExistsException e) {
				logger.info("Validation job of dataset " + directory.getName() + " already exists : ");
				// TODO send notification that probably the validation period is too short for jobs to finish
			}
		}
	}

	@Override
	public void submitComputeChecksumsJob(CloudDirectory directory) throws SchedulerException {
		synchronized (this.getClass()) {
			try {
				JobDetail job = newJob(ComputeChecksumsJob.class)
						.withIdentity(directory.getName(), "compute-checksums-job")
						.build();
				job.getJobDataMap().put("dataset", directory);
		
				Trigger trigger = newTrigger()
						.withIdentity(directory.getName(), "compute-checksums-trigger")
						.startNow()
						.build();
				
				scheduler.scheduleJob(job, trigger);
				
			} catch (ObjectAlreadyExistsException e) {
				logger.info("Computing checksums job of dataset " + directory.getName() + " already exists : ");
				// TODO send notification that probably the validation period is too short for jobs to finish
			}
		}
	}
	
	@Override
	public void submitUpdateSingleItemChecksumJob(CloudDirectory directory, CloudFile file) throws SchedulerException {
		synchronized (this.getClass()) {
			try {
				JobDetail job = newJob(UpdateSingleItemChecksumJob.class)
						.withIdentity(directory.getName() + ":" + file.getName(), "update-item-job")
						.build();
				job.getJobDataMap().put("dataset", directory);
				job.getJobDataMap().put("item", file);
		
				Trigger trigger = newTrigger()
						.withIdentity(directory.getName() + ":" + file.getName(), "update-item-trigger")
						.startNow()
						.build();
				
				scheduler.scheduleJob(job, trigger);
				
			} catch (ObjectAlreadyExistsException e) {
				logger.info("Updating checksum job of item " 
							+ file.getName() + ":" + directory.getName() + " already exists : ");
				// TODO send notification that probably the validation period is too short for jobs to finish
			}
		}
	}

}
