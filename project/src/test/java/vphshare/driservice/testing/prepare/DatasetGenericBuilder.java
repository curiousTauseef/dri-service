package vphshare.driservice.testing.prepare;

import vphshare.driservice.domain.DataSource;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.testing.MetadataRegistryMock;


public interface DatasetGenericBuilder {

	CloudDirectory build(MetadataRegistryMock registry, DataSource ds);
	
	void cleanup(CloudDirectory dataset, MetadataRegistryMock registry, DataSource ds);
}
