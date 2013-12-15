package vphshare.driservice.notification;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.log4j.Logger;
import vphshare.driservice.auth.AuthService;
import vphshare.driservice.notification.domain.Notification;

import javax.inject.Inject;
import javax.inject.Named;

public class MasterInterfaceNotificationService implements NotificationService {

    private static final Logger logger = Logger.getLogger(MasterInterfaceNotificationService.class);

    private WebResource notificationService;
    private AuthService authService;

    private String ticket;

    @Inject
    public MasterInterfaceNotificationService(
            @Named("mi-notification") WebResource notificationService,
            AuthService authService) {
        this.notificationService = notificationService;
        this.authService = authService;
        ticket = authService.getAuthTicket();
    }

    @Override
    public void sendNotification(Notification notification) {
        if (notification.getType() == Notification.Type.ADMIN) {
            throw new UnsupportedOperationException("MI does not handle admin notifications");
        }

        ClientResponse response = sendNotification(notification, ticket);
        // got auth error, try to refresh the auth ticket and retry
        if (response.getStatus() == 403) {
            logger.warn("Auth ticket expired! Refreshing the ticket...");
            ticket = authService.refreshAuthTicket(ticket);
            sendNotification(notification, ticket);
        }

        if (response.getStatus() != 200) {
            throw new RuntimeException(
                    String.format("Sending notification failure..., got [responseCode=%d]", response.getStatus()));
        }
    }

    private ClientResponse sendNotification(Notification notification, String ticket) {
        logger.info("Sending MasterInterface notification: " + notification);
        WebResource resource = notificationService
            .path("/api/notify/")
            .queryParam("ticket", ticket)
            .queryParam("recipient", notification.getRecipient())
            .queryParam("message", notification.getMessage())
            .queryParam("subject", notification.getSubject());
        logger.info("Using uri: " + resource.getURI());
        return resource.get(ClientResponse.class);
    }
}
