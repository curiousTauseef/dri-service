package vphshare.driservice.notification;

import com.google.inject.Inject;
import vphshare.driservice.notification.domain.Notification;

public class CombinedNotificationService implements NotificationService {

    private final EmailNotificationService emailNotificationService;
    private final MasterInterfaceNotificationService masterInterfaceNotificationService;

    @Inject
    public CombinedNotificationService(
            EmailNotificationService emailNotificationService,
            MasterInterfaceNotificationService masterInterfaceNotificationService) {
        this.emailNotificationService = emailNotificationService;
        this.masterInterfaceNotificationService = masterInterfaceNotificationService;
    }

    @Override
    public void sendNotification(Notification notification) {
        if (notification.getType() == Notification.Type.USER) {
            masterInterfaceNotificationService.sendNotification(notification);
        } else {
            emailNotificationService.sendNotification(notification);
        }
    }
}
