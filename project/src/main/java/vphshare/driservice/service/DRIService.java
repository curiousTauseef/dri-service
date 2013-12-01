package vphshare.driservice.service;

import com.google.inject.ImplementedBy;

import javax.ws.rs.core.Response;

@ImplementedBy(DRIServiceImpl.class)
public interface DRIService {
	
	Response addToManagement(String datasetID);
	
	Response removeFromManagement(String datasetID);

	Response updateChecksums(String datasetID);

	Response validateDataset(String datasetID);
	
	Response updateSingleItemChecksum(String datasetID, String itemID);
}
