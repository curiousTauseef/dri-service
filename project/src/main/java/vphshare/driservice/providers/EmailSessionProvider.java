package vphshare.driservice.providers;

import com.google.inject.Provider;

import javax.mail.Session;
import java.util.Properties;

public class EmailSessionProvider implements Provider<Session> {

    public EmailSessionProvider() {}

    @Override
    public Session get() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "kinga.cyf-kr.edu.pl");
        //props.put("mail.smtp.port", "465");

        return Session.getDefaultInstance(props, null);
    }
}
