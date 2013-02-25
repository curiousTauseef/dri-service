package vphshare.driservice.scheduler;

import static java.util.logging.Level.INFO;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.logging.Logger;

import javax.inject.Inject;

import org.quartz.JobDetail;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import vphshare.driservice.domain.LogicalData;
import vphshare.driservice.domain.ManagedDataset;
import vphshare.driservice.validation.ComputeChecksumsJob;
import vphshare.driservice.validation.DatasetValidationJob;
import vphshare.driservice.validation.UpdateSingleItemChecksumJob;

public class QuartzTaskSubmitter implements TaskSubmitter {

	private static final Logger LOG = Logger.getLogger(QuartzTaskSubmitter.class.getName());
	
	private Scheduler scheduler;

	@Inject
	public QuartzTaskSubmitter(PeriodicScheduler periodicScheduler) {
		scheduler = periodicScheduler.getScheduler();
	}
	
	@Override
	public void submitValidationJob(ManagedDataset dataset) throws SchedulerException {
		synchronized (this.getClass()) {
			try {
				JobDetail job = newJob(DatasetValidationJob.class)
						.withIdentity(dataset.getName(), "dataset-validation-job")
						.build();
				job.getJobDataMap().put("dataset", dataset);
	
				Trigger trigger = newTrigger()
						.withIdentity(dataset.getName(), "dataset-validation-trigger")
						.startNow()
						.build();
				
				LOG.log(INFO, "Scheduling job: " + job.getKey() + ", " + job.getDescription());
				scheduler.scheduleJob(job, trigger);
				
			} catch (ObjectAlreadyExistsException e) {
				LOG.log(INFO, "Validation job of dataset " + dataset.getName() + " already exists : ");
				// TODO send notification that probably the validation period is too short for jobs to finish
			}
		}
	}

	@Override
	public void submitComputeChecksumsJob(ManagedDataset dataset) throws SchedulerException {
		synchronized (this.getClass()) {
			try {
				JobDetail job = newJob(ComputeChecksumsJob.class)
						.withIdentity(dataset.getName(), "compute-checksums-job")
						.build();
				job.getJobDataMap().put("dataset", dataset);
		
				Trigger trigger = newTrigger()
						.withIdentity(dataset.getName(), "compute-checksums-trigger")
						.startNow()
						.build();
				
				scheduler.scheduleJob(job, trigger);
				
			} catch (ObjectAlreadyExistsException e) {
				LOG.log(INFO, "Computing checksums job of dataset " + dataset.getName() + " already exists : ");
				// TODO send notification that probably the validation period is too short for jobs to finish
			}
		}
	}
	
	@Override
	public void submitUpdateSingleItemChecksumJob(ManagedDataset dataset, LogicalData item) throws SchedulerException {
		synchronized (this.getClass()) {
			try {
				JobDetail job = newJob(UpdateSingleItemChecksumJob.class)
						.withIdentity(dataset.getName() + ":" + item.getIdentifier(), "update-item-job")
						.build();
				job.getJobDataMap().put("dataset", dataset);
				job.getJobDataMap().put("item", item);
		
				Trigger trigger = newTrigger()
						.withIdentity(dataset.getName() + ":" + item.getIdentifier(), "update-item-trigger")
						.startNow()
						.build();
				
				scheduler.scheduleJob(job, trigger);
				
			} catch (ObjectAlreadyExistsException e) {
				LOG.log(INFO, "Updating checksum job of item " 
							+ item.getIdentifier() + ":" + dataset.getName() + " already exists : ");
				// TODO send notification that probably the validation period is too short for jobs to finish
			}
		}
	}

}
