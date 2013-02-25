package vphshare.driservice.notification.domain;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import vphshare.driservice.domain.ManagedDataset;


public class Notification {

	private ManagedDataset dataset;
	private String title;
	private Date date;
	private long duration;
	private String content;
	private Map<String, ValidationStatus> entryLogs;
	
	public ManagedDataset getDataset() {
		return dataset;
	}
	
	public void setDataset(ManagedDataset dataset) {
		this.dataset = dataset;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = StringEscapeUtils.escapeHtml(content).replace("\n", "<br />");
	}

	public Map<String, ValidationStatus> getEntryLogs() {
		return entryLogs;
	}

	public void setEntryLogs(Map<String, ValidationStatus> entryLogs) {
		this.entryLogs = entryLogs;
	}
}
