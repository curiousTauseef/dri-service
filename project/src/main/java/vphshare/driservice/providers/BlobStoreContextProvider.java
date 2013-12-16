package vphshare.driservice.providers;

import com.google.common.collect.Lists;
import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.enterprise.config.EnterpriseConfigurationModule;
import org.jclouds.logging.config.NullLoggingModule;
import org.jclouds.openstack.swift.SwiftKeystoneApiMetadata;
import vphshare.driservice.domain.DataSource;
import vphshare.driservice.util.SwiftURL;

import java.util.Properties;

import static org.jclouds.Constants.*;

public class BlobStoreContextProvider {

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
        return ContextBuilder.newBuilder("aws-s3")
                .credentials(properties.getProperty("s3.accessid"), properties.getProperty("s3.accesskey"))
                .modules(Lists.newArrayList(new EnterpriseConfigurationModule(), new NullLoggingModule()))
                .overrides(overrides)
                .buildView(BlobStoreContext.class);
	}

	private static BlobStoreContext createSwiftBlobStore(DataSource ds) {
		Properties overrides = new Properties();
        String endpoint = ds.getResourceUrl().replace("swift", "http");
		overrides.setProperty(PROPERTY_ENDPOINT, endpoint);
		overrides.setProperty(PROPERTY_TRUST_ALL_CERTS, "true");
		overrides.setProperty(PROPERTY_RELAX_HOSTNAME, "true");
        overrides.setProperty(PROPERTY_CONNECTION_TIMEOUT, Integer.toString(5000));
		return ContextBuilder.newBuilder(new SwiftKeystoneApiMetadata())
                .endpoint(endpoint)
                .credentials(ds.getUsername(), ds.getPassword())
                .modules(Lists.newArrayList(new EnterpriseConfigurationModule(), new NullLoggingModule()))
                .overrides(overrides)
                .buildView(BlobStoreContext.class);
	}
}
