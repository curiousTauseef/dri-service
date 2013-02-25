package vphshare.driservice.notification.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import vphshare.driservice.domain.ManagedDataset;


public class DatasetReport {

	private ManagedDataset dataset;
	private ValidationStatus status;
	private String title;
	private long duration;
	private Map<String, ValidationStatus> entryLogs;
	
	public DatasetReport(ManagedDataset dataset) {
		this.dataset = dataset;
		entryLogs = new HashMap<String, ValidationStatus>();
		status = ValidationStatus.VALID;
	}
	
	public boolean isValid() {
		return status.isValid();
	}
	
	public ManagedDataset getDataset() {
		return dataset;
	}

	public void setDataset(ManagedDataset dataset) {
		this.dataset = dataset;
	}

	public ValidationStatus getStatus() {
		return status;
	}

	public void setStatus(ValidationStatus status) {
		this.status = status;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void addEntryLog(String key, ValidationStatus status) {
		if (!status.isValid())
			setStatus(ValidationStatus.INTEGRITY_ERROR);
		entryLogs.put(key, status);
	}
	
	public Map<String, ValidationStatus> getEntryLogs() {
		return Collections.unmodifiableMap(entryLogs);
	}
	
	public String logsToString() {
		StringBuilder text = new StringBuilder();
		for (Map.Entry<String, ValidationStatus> item : entryLogs.entrySet()) {
			text.append(item.getKey() + "\t");
			text.append(item.getValue().toString());
			text.append("\n");
		}
		return text.toString();
	}
}
