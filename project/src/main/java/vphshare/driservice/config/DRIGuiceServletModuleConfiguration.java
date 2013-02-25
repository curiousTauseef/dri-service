package vphshare.driservice.config;

import static com.google.inject.Scopes.SINGLETON;
import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;
import static com.google.inject.matcher.Matchers.subclassesOf;
import static com.google.inject.name.Names.bindProperties;
import static com.google.inject.name.Names.named;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import vphshare.driservice.aop.DRIServiceExceptionHandler;
import vphshare.driservice.aop.MetadataRegistryExceptionHandler;
import vphshare.driservice.providers.AIRConfigurationProvider;
import vphshare.driservice.providers.ExecutorProvider;
import vphshare.driservice.providers.PropertiesProvider;
import vphshare.driservice.registry.AIRMetadataRegistry;
import vphshare.driservice.scheduler.InjectingJobFactory;
import vphshare.driservice.scheduler.PeriodicScheduler;
import vphshare.driservice.service.DRIServiceImpl;
import vphshare.driservice.validation.CustomValidationStrategy;
import vphshare.driservice.validation.ValidationStrategy;

import com.google.inject.AbstractModule;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class DRIGuiceServletModuleConfiguration extends JerseyServletModule {

	@Override
	protected void configureServlets() {

		// REST resource bindings
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("com.sun.jersey.config.property.packages", "vphshare.driservice.service");
		serve("/dri/*").with(GuiceContainer.class, parameters);
		
		install(new AbstractModule() {
			
			@Override
			protected void configure() {
				bind(DRIServiceImpl.class);
				bind(ValidationStrategy.class).to(CustomValidationStrategy.class);
				
				// Properties bindings
				bindProperties(binder(), PropertiesProvider.getProperties());
				
				// Providers bindings
				bind(ExecutorService.class).toProvider(ExecutorProvider.class);
				bind(WebResource.class)
					.annotatedWith(named("air-config"))
					.toProvider(AIRConfigurationProvider.class);
				
				// run on startup quartz scheduler bindings
				bind(SchedulerFactory.class).to(StdSchedulerFactory.class).in(SINGLETON);
				bind(InjectingJobFactory.class).in(SINGLETON);
				bind(PeriodicScheduler.class).in(SINGLETON);
				bind(PeriodicScheduler.class).asEagerSingleton();
				
				// AOP exception handling
				
				MetadataRegistryExceptionHandler mreHandler = new MetadataRegistryExceptionHandler();
				requestInjection(mreHandler);
				bindInterceptor(
						subclassesOf(AIRMetadataRegistry.class), 
						any(), 
						mreHandler);
				
				DRIServiceExceptionHandler dseHandler = new DRIServiceExceptionHandler();
				requestInjection(dseHandler);
				bindInterceptor(
						annotatedWith(Path.class),
						annotatedWith(GET.class)
						.or(annotatedWith(POST.class))
						.or(annotatedWith(PUT.class))
						.or(annotatedWith(DELETE.class)),
						dseHandler);
			}
		});
	}
}
