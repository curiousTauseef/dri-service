package vphshare.driservice.testing.prepare;

import vphshare.driservice.domain.DataSource;
import vphshare.driservice.domain.ManagedDataset;
import vphshare.driservice.testing.MetadataRegistryMock;


public interface DatasetGenericBuilder {

	ManagedDataset build(MetadataRegistryMock registry, DataSource ds);
	
	void cleanup(ManagedDataset dataset, MetadataRegistryMock registry, DataSource ds);
}
