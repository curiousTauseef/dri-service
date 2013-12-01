package vphshare.driservice.providers;

import static org.jclouds.Constants.PROPERTY_ENDPOINT;
import static org.jclouds.Constants.PROPERTY_RELAX_HOSTNAME;
import static org.jclouds.Constants.PROPERTY_TRUST_ALL_CERTS;

import java.util.Properties;

import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.BlobStoreContextFactory;
import org.jclouds.enterprise.config.EnterpriseConfigurationModule;
import org.jclouds.logging.config.NullLoggingModule;

import vphshare.driservice.domain.DataSource;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

public class BlobStoreContextProvider {

	private static final BlobStoreContextFactory contextFactory = new BlobStoreContextFactory();
	private static Properties properties = PropertiesProvider.getProperties();
	
	private BlobStoreContextProvider() {}

	public static BlobStoreContext getContext(DataSource ds) {
		if (ds.getResourceUrl().startsWith("swift://")) {
			return createSwiftBlobStore(ds);
		} else {
			throw new UnsupportedOperationException("Unsupported cloud store yet!");
		}
	}

	@SuppressWarnings("unused")
	private static BlobStoreContext createS3BlobStore(DataSource ds) {
		Properties overrides = new Properties();
		overrides.setProperty(PROPERTY_TRUST_ALL_CERTS, "true");
		overrides.setProperty(PROPERTY_RELAX_HOSTNAME, "true");
		BlobStoreContext context = contextFactory.createContext("aws-s3",
				properties.getProperty("s3.accessid"),
				properties.getProperty("s3.accesskey"),
				// null logging module to get rid of nasty warnings using Swift...
				ImmutableSet.<Module> of(new EnterpriseConfigurationModule(), new NullLoggingModule()),
				overrides);
		return context;
	}

	private static BlobStoreContext createSwiftBlobStore(DataSource ds) {
		Properties overrides = new Properties();
		String endpoint = ds.getResourceUrl().replace("swift", "https");
		overrides.setProperty(PROPERTY_ENDPOINT, endpoint);
		overrides.setProperty(PROPERTY_TRUST_ALL_CERTS, "true");
		overrides.setProperty(PROPERTY_RELAX_HOSTNAME, "true");
		return contextFactory.createContext("swift",
				ds.getUsername(),
				ds.getPassword(),
				// null logging module to get rid of nasty warnings using Swift...
				ImmutableSet.<Module> of(new EnterpriseConfigurationModule(), new NullLoggingModule()),
				overrides);
	}
}
