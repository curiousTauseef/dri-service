package vphshare.driservice.validation;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;

import java.util.logging.Logger;

import javax.inject.Inject;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.notification.NotificationService;
import vphshare.driservice.notification.domain.DatasetReport;
import vphshare.driservice.registry.MetadataRegistry;

@DisallowConcurrentExecution
public class UpdateSingleItemChecksumJob implements Job {

	private static final Logger LOG = Logger.getLogger(UpdateSingleItemChecksumJob.class.getName());

	@Inject
	private DatasetValidator strategy;
	@Inject
	private NotificationService notificationService;
	@Inject
	private MetadataRegistry registry;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		CloudDirectory dataset = (CloudDirectory) context.getJobDetail().getJobDataMap().get("dataset");
		CloudFile item = (CloudFile) context.getJobDetail().getJobDataMap().get("item");
		doTheTask(dataset, item);
	}

	private void doTheTask(CloudDirectory dataset, CloudFile item) {
		LOG.log(INFO, "Updating checksum job for item " + dataset.getName() + ":" + item.getName());
		try {
			long start = System.currentTimeMillis();
			DatasetReport report = strategy.computeChecksum(dataset, item);
			long end = System.currentTimeMillis();
			
			report.setDuration((end - start) / 1000L);
		
			notificationService.notifyAboutComputedChecksums(report);
			
		} catch (Exception e) {
			LOG.log(SEVERE, "Unexpected exception thrown while updating checksum of item " 
					+ dataset.getName() + ":" + item.getName(), e);
			notificationService.notifyProcessingException(dataset, e);
		}
	}

}