package vphshare.driservice.testing;

import static com.google.inject.Scopes.SINGLETON;
import static com.google.inject.name.Names.bindProperties;

import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;

import vphshare.driservice.domain.DataSource;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.notification.domain.DatasetReport;
import vphshare.driservice.providers.BlobStoreContextProvider;
import vphshare.driservice.providers.PropertiesProvider;
import vphshare.driservice.registry.MetadataRegistry;
import vphshare.driservice.testing.prepare.DatasetGenericBuilder;
import vphshare.driservice.testing.prepare.SmallDatasetBuilder;
import vphshare.driservice.validation.CustomValidationStrategy;
import vphshare.driservice.validation.DatasetValidator;
import vphshare.driservice.validation.ValidationStrategy;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class CoreTesting {
	
	public static void main(String[] args) {
		new CoreTesting().simpleTest();
	}

	public void simpleTest() {
		Injector injector = getInjector();
		
		// 1. Preparing
		DataSource ds = new DataSource();
		ds.setResourceUrl("https://149.156.10.131:8443/auth/v1.0");
		
		MetadataRegistryMock registry = (MetadataRegistryMock) injector.getInstance(MetadataRegistry.class);
		
		DatasetGenericBuilder dp = new SmallDatasetBuilder();
		CloudDirectory dataset = dp.build(registry, ds);
		
		// 2. Computing checksums
		DatasetValidator validator = injector.getInstance(DatasetValidator.class);
		validator.computeChecksums(dataset);
		
		// 3. Validating
		DatasetReport report = validator.validate(dataset);
		
		// 4. Cleanup
		clearBlobstore(ds, dataset);
		
		assert report.isValid();
	}

	private void clearBlobstore(DataSource ds, CloudDirectory dataset) {
		BlobStoreContext context = BlobStoreContextProvider.getContext(ds);
		
		try {
			BlobStore blobstore = context.getBlobStore();
			blobstore.deleteContainer(dataset.getName());
		
		} finally {
			context.close();
		}
	}

	private static Injector getInjector() {
		Injector injector = Guice.createInjector(new AbstractModule() {
			
			@Override
			protected void configure() {
				bind(MetadataRegistry.class).to(MetadataRegistryMock.class).in(SINGLETON);
				bind(ValidationStrategy.class).to(CustomValidationStrategy.class);
				bindProperties(binder(), PropertiesProvider.getProperties());
			}
		});
		return injector;
	}
}
