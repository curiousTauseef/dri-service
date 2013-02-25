package vphshare.driservice.validation;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;

import java.util.logging.Logger;

import javax.inject.Inject;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import vphshare.driservice.domain.ManagedDataset;
import vphshare.driservice.notification.NotificationService;
import vphshare.driservice.notification.domain.DatasetReport;

@DisallowConcurrentExecution
public class DatasetValidationJob implements Job {
	
	private static final Logger LOG = Logger.getLogger(DatasetValidationJob.class.getName());

	@Inject
	private DatasetValidator strategy;
	@Inject
	private NotificationService notificationService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ManagedDataset dataset = (ManagedDataset) context.getJobDetail().getJobDataMap().get("dataset");
		LOG.log(INFO, "Validation job for dataset: " + dataset.getName());
		doTheTask(dataset);
	}

	private void doTheTask(ManagedDataset dataset) {
		try {
			long start = System.currentTimeMillis();
			DatasetReport report = strategy.validate(dataset);
			long end = System.currentTimeMillis();
			
			report.setDuration((end - start) / 1000L);
			
			if (report.isValid()) {
				notificationService.notifyAsValid(report);
			} else {
				notificationService.notifyAsInvalid(report);
			}

		} catch (Exception e) {
			LOG.log(SEVERE, "Unexpected exception thrown while validating dataset "
					+ dataset.getId(), e);
			notificationService.notifyProcessingException(dataset, e);
		}
	}
}