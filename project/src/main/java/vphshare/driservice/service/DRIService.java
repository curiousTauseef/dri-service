package vphshare.driservice.service;

import javax.ws.rs.core.Response;

import org.quartz.SchedulerException;

import com.google.inject.ImplementedBy;

@ImplementedBy(DRIServiceImpl.class)
public interface DRIService {
	
	Response addToManagement(String datasetID) throws SchedulerException;
	
	Response removeFromManagement(String datasetID);

	Response updateChecksums(String datasetID) throws SchedulerException;

	Response validateDataset(String datasetID) throws SchedulerException;
	
	Response updateSingleItemChecksum(String datasetID, String itemID) throws SchedulerException;
}
