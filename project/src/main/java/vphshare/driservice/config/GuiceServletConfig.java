package vphshare.driservice.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import vphshare.driservice.providers.PropertiesProvider;

public class GuiceServletConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
        // Certificate truststore
        System.setProperty("javax.net.ssl.trustStore",
                PropertiesProvider.getProperties().getProperty("javax.net.ssl.trustStore"));
        System.setProperty("javax.net.ssl.trustStorePassword",
                PropertiesProvider.getProperties().getProperty("javax.net.ssl.trustStorePassword"));

		return Guice.createInjector(new DRIGuiceServletModuleConfiguration());
	}
}
