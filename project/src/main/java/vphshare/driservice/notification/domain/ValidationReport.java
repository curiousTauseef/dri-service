package vphshare.driservice.notification.domain;

import vphshare.driservice.domain.CloudDirectory;

import java.util.HashMap;
import java.util.Map;


public class ValidationReport {

	private CloudDirectory dataset;
	private ValidationStatus status = ValidationStatus.VALID;
	private Map<String, ValidationStatus> entryLogs = new HashMap<String, ValidationStatus>();
	
	public ValidationReport(CloudDirectory dataset) {
		this.dataset = dataset;
	}
	
	public boolean isValid() {
		return status.isValid();
	}
	
	public CloudDirectory getDataset() {
		return dataset;
	}

	public ValidationStatus getStatus() {
		return status;
	}

	public void setStatus(ValidationStatus status) {
		this.status = status;
	}
	
	public void addEntryLog(String key, ValidationStatus status) {
		if (!status.isValid())
			setStatus(ValidationStatus.INTEGRITY_ERROR);
		entryLogs.put(key, status);
	}

    public Map<String, ValidationStatus> getEntryLogs() {
        return entryLogs;
    }
}
