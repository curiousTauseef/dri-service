package vphshare.driservice.notification;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.exceptions.AppException;
import vphshare.driservice.notification.domain.DatasetReport;
import vphshare.driservice.notification.domain.Notification;

public class DefaultNotificationService implements NotificationService {
	
	private final NotificationHolder container = new NotificationHolder();
	
	@Override
	public void notifyAboutComputedChecksums(DatasetReport report) {
		Notification notification = new Notification();
		notification.setDataset(report.getDataset());
		notification.setDate(new Date());
		notification.setDuration(report.getDuration());
		
		if (report.isValid()) {
			notification.setTitle("Computed dataset checksums");
			notification.setContent("Successfully computed " + report.getDataset().getName() + " checksums.");
		} else {
			notification.setTitle("Errors while computing checksums");
			StringBuilder text = new StringBuilder();
			text.append("Probably metadata in AIR are not consistent with the data source content:\n\n");
			notification.setContent(text.toString());
			notification.setEntryLogs(report.getEntryLogs());
		}
		
		container.addNotification(notification);
	}

	@Override
	public void notifyAsValid(DatasetReport report) {
		Notification notification = new Notification();
		notification.setDataset(report.getDataset());
		notification.setTitle("Validation success");
		notification.setDate(new Date());
		notification.setDuration(report.getDuration());
		
		CloudDirectory dataset = report.getDataset();
		StringBuilder text = new StringBuilder();
		text.append("The dataset ");
		text.append(dataset.getName());
		text.append(" is valid.\n");
		notification.setContent(text.toString());
		container.addNotification(notification);
	}

	@Override
	public void notifyAsInvalid(DatasetReport report) {
		CloudDirectory dataset = report.getDataset();
		
		Notification notification = new Notification();
		notification.setDataset(dataset);
		notification.setTitle("Integrity errors detected");
		
		StringBuilder text = new StringBuilder();
		text.append("The dataset " + dataset.getName() + " is ");
		
		if (report.getStatus().isAvailable()) {
			text.append(report.getStatus().toString());
			text.append("\n\n");
			text.append("Below is the detailed validation report:\n");
			
		} else {
			text.append(report.getStatus().toString());
			text.append("\n\n");
		}
		
		notification.setContent(text.toString());
		notification.setDate(new Date());
		notification.setDuration(report.getDuration());
		notification.setEntryLogs(report.getEntryLogs());
		container.addNotification(notification);
	}

	@Override
	public void notifyProcessingException(CloudDirectory dataset, Exception e) {
		Notification notification = new Notification();
		notification.setDataset(dataset);
		notification.setTitle("Processing exception occured");
		notification.setDate(new Date());
		
		StringWriter text = new StringWriter();
		e.printStackTrace(new PrintWriter(text));
		notification.setContent(text.toString());
		container.addNotification(notification);
	}

	@Override
	public void notifyException(AppException ae) {
	}
}
