package vphshare.driservice.auth;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

import javax.inject.Inject;
import javax.inject.Named;

public class AuthServiceImpl implements AuthService {

    private final WebResource service;
    private final String username;
    private final String password;

    @Inject
    public AuthServiceImpl(
            @Named("auth-config") WebResource service,
            @Named("auth.username") String username,
            @Named("auth.password") String password) {
        this.service = service;
        this.username = username;
        this.password = password;
    }

    @Override
    public String getAuthTicket() {
        Form form = new Form();
        form.add("username", username);
        form.add("password", password);
        String ticket =  service
                .path("user_login")
                .post(String.class, form);
        System.out.println("Got ticket=" + ticket);
        return ticket;
    }

    @Override
    public String refreshAuthTicket(String ticket) {
        Form form = new Form();
        form.add("ticket", ticket);
        return service
                .path("refresh_tkt")
                .post(String.class, form);
    }
}
