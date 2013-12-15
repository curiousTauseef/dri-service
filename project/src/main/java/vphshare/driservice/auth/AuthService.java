package vphshare.driservice.auth;

import com.google.inject.ImplementedBy;

@ImplementedBy(AuthServiceImpl.class)
public interface AuthService {

    String getAuthTicket();

    String refreshAuthTicket(String ticket);
}
