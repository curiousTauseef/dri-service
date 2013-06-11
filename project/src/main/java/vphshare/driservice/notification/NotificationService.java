package vphshare.driservice.notification;

import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.exceptions.AppException;
import vphshare.driservice.notification.domain.DatasetReport;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultNotificationService.class)
public interface NotificationService {
	
	void notifyAboutComputedChecksums(DatasetReport report);
	
	void notifyAsValid(DatasetReport report);
	
	void notifyAsInvalid(DatasetReport report);
	
	void notifyProcessingException(CloudDirectory dataset, Exception e);
	
	void notifyException(AppException ae);
}
