package vphshare.driservice.providers;

import javax.inject.Inject;
import javax.inject.Named;

import vphshare.driservice.util.JsonPlainTextEnablingProvider;

import com.google.inject.Provider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class LobcderConfigurationProvider implements Provider<WebResource> {
	
	@Inject @Named("lobcder.baseurl") 
	private String baseURL;
	@Inject @Named("lobcder.username")
	private String username;
	@Inject @Named("lobcder.password")
	private String password;
	@Inject @Named("lobcder.read_timeout")
	private int readTimeout;
	
	public LobcderConfigurationProvider() {}

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
