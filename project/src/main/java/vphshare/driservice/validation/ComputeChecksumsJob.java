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
import vphshare.driservice.registry.MetadataRegistry;

@DisallowConcurrentExecution
public class ComputeChecksumsJob implements Job {

	private static final Logger LOG = Logger.getLogger(DatasetValidationJob.class.getName());

	@Inject
	private DatasetValidator validator;
	@Inject
	private NotificationService notificationService;
	@Inject
	private MetadataRegistry registry;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ManagedDataset dataset = (ManagedDataset) context.getJobDetail().getJobDataMap().get("dataset");
		doTheTask(dataset);
	}

	private void doTheTask(ManagedDataset dataset) {
		LOG.log(INFO, "Compute dataset checksums job for dataset: " + dataset.getName());
		try {
			long start = System.currentTimeMillis();
			DatasetReport report = validator.computeChecksums(dataset);
			long end = System.currentTimeMillis();
			
			report.setDuration((end - start) / 1000L);
			
			if (!report.isValid())
				registry.unsetAsManaged(dataset);
			
			notificationService.notifyAboutComputedChecksums(report);
			
		} catch (Exception e) {
			LOG.log(SEVERE, "Unexpected exception thrown while computing checksums of dataset "
					+ dataset.getName(), e);
			notificationService.notifyProcessingException(dataset, e);
		}
	}

}
