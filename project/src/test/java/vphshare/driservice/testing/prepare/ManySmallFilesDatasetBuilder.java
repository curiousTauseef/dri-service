package vphshare.driservice.testing.prepare;

import java.util.ArrayList;
import java.util.List;

import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;

import vphshare.driservice.domain.DataSource;
import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.providers.BlobStoreContextProvider;
import vphshare.driservice.testing.MetadataRegistryMock;

public class ManySmallFilesDatasetBuilder implements DatasetGenericBuilder {

	@Override
	public CloudDirectory build(MetadataRegistryMock registry, DataSource ds) {
		BlobStoreContext context = BlobStoreContextProvider.getContext(ds);

		// 100 KB each file
		final int filesize = 1024 * 100;
		// 10000 * 100 KB ~ 1GB
		final int numberOfFiles = 10000;
		try {
			BlobStore blobstore = context.getBlobStore();
			
			CloudDirectory dataset = new CloudDirectory("1", "many-small-files-dataset");
	
			blobstore.createContainerInLocation(null, dataset.getName());
			registry.addDataset(dataset);
			
			byte[] payload = new byte[filesize];
			List<CloudFile> datas = new ArrayList<CloudFile>();
	
			for (int i = 0; i < numberOfFiles; i++) {
				CloudFile item = new CloudFile("" + i);
				item.setName("item" + i);
				item.setSize(payload.length);
				List<DataSource> dsList = new ArrayList<DataSource>();
				dsList.add(ds);
				item.setDataSources(dsList);
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
	public void cleanup(CloudDirectory dataset, MetadataRegistryMock registry, DataSource ds) {
		BlobStoreContext context = BlobStoreContextProvider.getContext(ds);
		
		try {
			BlobStore blobstore = context.getBlobStore();
			blobstore.deleteContainer(dataset.getName());
		
		} finally {
			context.close();
		}
	}
}
