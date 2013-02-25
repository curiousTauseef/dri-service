package vphshare.driservice.scheduler;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static vphshare.driservice.registry.AIRMetadataRegistry.DatasetCategory.ONLY_MANAGED;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import vphshare.driservice.domain.ManagedDataset;
import vphshare.driservice.registry.MetadataRegistry;

public class AllDatasetsValidationJob implements Job {
	
	private static final Logger LOG = Logger.getLogger(AllDatasetsValidationJob.class.getName());
	
	@Inject
	private MetadataRegistry registry;
	@Inject 
	private TaskSubmitter submitter;
	
	@Override
	public void execute(final JobExecutionContext context) throws JobExecutionException {
		try {
			List<ManagedDataset> datasets = registry.getDatasets(ONLY_MANAGED);
			Collections.sort(datasets);
			
			LOG.log(INFO, "Number of managed datasets: " + datasets.size());
			
			for (ManagedDataset dataset : datasets) {
				submitter.submitValidationJob(dataset);
			}
			
		} catch (Exception e) {
			LOG.log(SEVERE, "Unexpected exception at invoking periodic validation jobs!", e);
		}
	}

}
