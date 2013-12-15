package vphshare.driservice.auth;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import vphshare.driservice.util.JsonPlainTextEnablingProvider;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class AuthConfigProvider implements Provider<WebResource> {

    @Inject @Named("auth.baseurl")
    private String baseURL;
    @Inject @Named("auth.read_timeout")
    private int readTimeout;

    public AuthConfigProvider() {}

    @Override
    public WebResource get() {
        try {
            TrustManager[ ] certs = new TrustManager[ ] {
                    new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                        }
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                        }
                    }
            };
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(null, certs, null);
            ClientConfig config = new DefaultClientConfig();
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            },ctx));
            config.getClasses().add(JsonPlainTextEnablingProvider.class);

            Client client = Client.create(config);
            client.setReadTimeout(readTimeout);

            return client.resource(baseURL);
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException(nsae);
        } catch (KeyManagementException kme) {
            throw new RuntimeException(kme);
        }
    }
}
