package vphshare.driservice.auth;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Named;

public class AuthServiceImpl implements AuthService {

    private static final Logger logger = Logger.getLogger(AuthServiceImpl.class);

    private final WebResource service;
    private final String username;
    private final String password;

    private String ticket;
    private DateTime lastTicketRefresh;

    @Inject
    public AuthServiceImpl(
            @Named("auth-config") WebResource service,
            @Named("auth.username") String username,
            @Named("auth.password") String password) {
        this.service = service;
        this.username = username;
        this.password = password;
        this.ticket = retrieveAuthTicket();
        lastTicketRefresh = DateTime.now();
    }

    private String retrieveAuthTicket() {
        Form form = new Form();
        form.add("username", username);
        form.add("password", password);
        String ticket =  service
                .path("user_login")
                .post(String.class, form);
        logger.info("Got ticket=" + ticket);
        return ticket;
    }

    @Override
    public synchronized String getAuthTicket() {
        return ticket;
    }

    @Override
    public synchronized String refreshAuthTicket(String oldTicket) {
        if (lastTicketRefresh.plusMinutes(30).isBeforeNow()) {
            logger.info("Refreshing auth ticket");
            Form form = new Form();
            form.add("ticket", oldTicket);
            ticket =  service
                    .path("refresh_tkt")
                    .post(String.class, form);
            lastTicketRefresh = DateTime.now();
            logger.info("Refreshed ticket: " + ticket);
        }
        return ticket;
    }
}
