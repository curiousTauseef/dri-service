package vphshare.driservice.notification;

import vphshare.driservice.notification.domain.Notification;

import com.google.inject.ImplementedBy;

public interface NotificationService {

	void sendNotification(Notification notification);
}
