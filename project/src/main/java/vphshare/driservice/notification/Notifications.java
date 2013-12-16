package vphshare.driservice.notification;

import org.apache.commons.lang.exception.ExceptionUtils;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.notification.domain.Notification;

import static java.lang.String.format;

public class Notifications {

    public static Notification datasetInvalid(CloudDirectory directory) {
        String recipient = directory.getOwner();
        String message = format("Directory [%s] seems corrupted! Please check", directory.getName());
        String subject = "";
        return new Notification(
                Notification.Type.USER,
                recipient,
                subject,
                message
        );
    }

    public static Notification superviseDatasetFailed(CloudDirectory directory, Exception e) {
        String recipient = directory.getOwner();
        String message = format("Adding directory [%s] under supervision failed!", directory.getName());
        String subject = "";
        return new Notification(
                Notification.Type.USER,
                recipient,
                subject,
                message
        );
    }

    public static Notification internalError(Exception e) {
        String recipient = "kstyrc@gmail.com";
        String subject = "[DRI] DRI service error, please investigate";
        String message = format("Message: [%s]%nStackTrace:%n%s", e.getMessage(), ExceptionUtils.getStackTrace(e));
        return new Notification(
                Notification.Type.ADMIN,
                recipient,
                subject,
                message
        );
    }
}
