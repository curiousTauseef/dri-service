package vphshare.driservice.providers;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import vphshare.driservice.util.JsonPlainTextEnablingProvider;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

public class MasterInterfaceConfigurationProvider implements Provider<WebResource> {

    @Inject @Named("mi.baseurl")
    private String baseURL;
    @Inject @Named("mi.username")
    private String username;
    @Inject @Named("mi.password")
    private String password;
    @Inject @Named("mi.read_timeout")
    private int readTimeout;

    public MasterInterfaceConfigurationProvider() {}

    @Override
    public WebResource get() {
        HTTPBasicAuthFilter httpBasicAuthFilter = new HTTPBasicAuthFilter(username, password);
        ClientConfig config = new DefaultClientConfig();
        config.getClasses().add(JsonPlainTextEnablingProvider.class);

        Client client = Client.create(config);
        client.setReadTimeout(readTimeout);
        client.addFilter(httpBasicAuthFilter);

        return client.resource(baseURL);
    }
}
