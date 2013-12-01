package vphshare.driservice.notification;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.uri.UriTemplate;
import org.apache.log4j.Logger;
import vphshare.driservice.notification.domain.Notification;

import javax.inject.Inject;
import javax.inject.Named;

public class MasterInterfaceNotificationService  implements NotificationService {

    private static final Logger logger = Logger.getLogger(MasterInterfaceNotificationService.class);

    protected WebResource service;

    @Inject
    public MasterInterfaceNotificationService(@Named("mi-notification") WebResource service) {
        this.service = service;
    }

    @Override
    public void sendNotification(Notification notification) {
        if (notification.getType() == Notification.Type.ADMIN) {
            throw new UnsupportedOperationException("MI does not handle admin notifications");
        }

        logger.info("Sending MasterInterface notification: " + notification);
        ClientResponse response = service
            .path("/api/notify")
            .queryParam("ticket", "123456")
            .queryParam("recipient", notification.getRecipient())
            .queryParam("message", notification.getMessage())
            .queryParam("subject", notification.getSubject())
            .get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException(
                    String.format("Sending notification failure..., got [responseCode=%d]", response.getStatus()));
        }
    }
}
