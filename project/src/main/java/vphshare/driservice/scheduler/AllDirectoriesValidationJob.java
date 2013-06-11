package vphshare.driservice.scheduler;


import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.registry.MetadataRegistry;

public class AllDirectoriesValidationJob implements Job {
	
	private static final Logger logger = Logger.getLogger(AllDirectoriesValidationJob.class);
	
	@Inject
	private MetadataRegistry registry;
	@Inject 
	private TaskSubmitter submitter;
	
	@Override
	public void execute(final JobExecutionContext context) throws JobExecutionException {
		try {
			List<CloudDirectory> dirs = registry.getCloudDirectories(true);
			Collections.sort(dirs);
			
			for (CloudDirectory dir : dirs) {
				submitter.submitValidationJob(dir);
			}
			
		} catch (Exception e) {
			logger.error("Unexpected exception at invoking periodic validation jobs!", e);
		}
	}

}
