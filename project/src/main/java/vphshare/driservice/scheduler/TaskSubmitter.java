package vphshare.driservice.scheduler;

import org.quartz.SchedulerException;

import com.google.inject.ImplementedBy;

import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.domain.CloudDirectory;

@ImplementedBy(QuartzTaskSubmitter.class)
public interface TaskSubmitter {

	void submitValidationJob(CloudDirectory directory) throws SchedulerException;
	
	void submitComputeChecksumsJob(CloudDirectory directory) throws SchedulerException;
	
	void submitUpdateSingleItemChecksumJob(CloudDirectory directory, CloudFile item) throws SchedulerException;
}
