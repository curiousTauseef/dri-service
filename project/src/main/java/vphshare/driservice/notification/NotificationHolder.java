package vphshare.driservice.notification;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import vphshare.driservice.notification.domain.Notification;

public class NotificationHolder {
	
	private static final int MAX_NOTIFICATIONS = 100;

	private static final LinkedList<Notification> notifications = new LinkedList<Notification>();
	
	synchronized public void addNotification(Notification notification) {
		if (notifications.size() == MAX_NOTIFICATIONS)
			notifications.removeLast();
		notifications.addFirst(notification);
	}
	
	synchronized public List<Notification> getNotifications() {
		return Collections.unmodifiableList(notifications);
	}
}
