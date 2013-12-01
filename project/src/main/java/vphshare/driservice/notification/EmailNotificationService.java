package vphshare.driservice.notification;

import org.apache.log4j.Logger;
import vphshare.driservice.notification.domain.Notification;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailNotificationService implements NotificationService {
    private static final Logger logger = Logger.getLogger(EmailNotificationService.class);

    private final Session mailSession;

    @Inject
    public EmailNotificationService(@Named("email-notification") Session mailSession) {
        this.mailSession = mailSession;
    }

    @Override
    public void sendNotification(Notification notification) {
        try {
            logger.info("Sending email notification: " + notification);

            Message message = new MimeMessage(mailSession);
            InternetAddress fromAddress = new InternetAddress("dri@vph-share.eu");
            InternetAddress toAddress = new InternetAddress(notification.getRecipient());

            message.setFrom(fromAddress);
            message.setRecipient(Message.RecipientType.TO, toAddress);
            message.setSubject(notification.getSubject());
            message.setText(notification.getMessage());

            Transport.send(message);
            logger.info("Sent email notification: " + notification);

        } catch (MessagingException me) {
            logger.error("Failed to sent email notification: " + notification, me);
            throw new RuntimeException(me);
        }
    }
}
