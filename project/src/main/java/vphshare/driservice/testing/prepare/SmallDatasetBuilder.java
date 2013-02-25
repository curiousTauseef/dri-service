package vphshare.driservice.testing.prepare;

import java.util.ArrayList;
import java.util.List;

import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;

import vphshare.driservice.domain.DataSource;
import vphshare.driservice.domain.LogicalData;
import vphshare.driservice.domain.ManagedDataset;
import vphshare.driservice.providers.BlobStoreContextProvider;
import vphshare.driservice.testing.MetadataRegistryMock;

public class SmallDatasetBuilder implements DatasetGenericBuilder {

	@Override
	public ManagedDataset build(MetadataRegistryMock registry, DataSource ds) {
		
		BlobStoreContext context = BlobStoreContextProvider.getContext(ds);

		try {
			BlobStore blobstore = context.getBlobStore();
			
			ManagedDataset dataset = new ManagedDataset("1", "small-dataset");
			List<DataSource> dsList = new ArrayList<DataSource>();
			dsList.add(ds);
			dataset.setDataSources(dsList);
	
			blobstore.createContainerInLocation(null, dataset.getName());
			registry.addDataset(dataset);
			
			byte[] payload = new byte[100000];
			List<LogicalData> datas = new ArrayList<LogicalData>();
	
			for (int i = 0; i < 10; i++) {
				LogicalData item = new LogicalData("" + i);
				item.setIdentifier("item" + i);
				item.setSize(payload.length);
				Blob blob = blobstore.blobBuilder("item" + i).payload(payload).build();
				blobstore.putBlob(dataset.getName(), blob);
				datas.add(item);
			}
			
			registry.addLogicalDatas(dataset, datas);
			return dataset;
		
		} finally {
			context.close();
		}
	}

	@Override
	public void cleanup(ManagedDataset dataset, MetadataRegistryMock registry, DataSource ds) {
		BlobStoreContext context = BlobStoreContextProvider.getContext(ds);
		
		try {
			BlobStore blobstore = context.getBlobStore();
			blobstore.deleteContainer(dataset.getName());
		
		} finally {
			context.close();
		}
	}
}
