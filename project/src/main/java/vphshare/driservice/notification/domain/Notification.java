package vphshare.driservice.notification.domain;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;


public class Notification {

    public static enum Type {
        USER, ADMIN
    }

    private Type type;
    private String recipient;
	private String subject;
    private String message;

    public Notification(Type type, String recipient, String subject, String message) {
        this.type = type;
        this.recipient = recipient;
        this.subject = subject;
        this.message = escapeHtml(message).replace("\n", "<br />");
    }

    public Type getType() {
        return type;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "type=" + type +
                ", recipient='" + recipient + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
