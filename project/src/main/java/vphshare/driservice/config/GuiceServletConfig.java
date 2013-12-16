package vphshare.driservice.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import vphshare.driservice.providers.PropertiesProvider;

public class GuiceServletConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new DRIGuiceServletModuleConfiguration());
	}
}
