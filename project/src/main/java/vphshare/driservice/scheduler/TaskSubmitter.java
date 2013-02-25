package vphshare.driservice.scheduler;

import org.quartz.SchedulerException;

import com.google.inject.ImplementedBy;

import vphshare.driservice.domain.LogicalData;
import vphshare.driservice.domain.ManagedDataset;

@ImplementedBy(QuartzTaskSubmitter.class)
public interface TaskSubmitter {

	void submitValidationJob(ManagedDataset dataset) throws SchedulerException;
	
	void submitComputeChecksumsJob(ManagedDataset dataset) throws SchedulerException;
	
	void submitUpdateSingleItemChecksumJob(ManagedDataset dataset, LogicalData item) throws SchedulerException;
}
