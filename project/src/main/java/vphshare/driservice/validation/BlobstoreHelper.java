package vphshare.driservice.validation;

import static org.jclouds.blobstore.options.GetOptions.Builder.range;

import java.io.InputStream;

import org.apache.commons.io.input.AutoCloseInputStream;
import org.jclouds.blobstore.AsyncBlobStore;
import org.jclouds.blobstore.domain.Blob;

import vphshare.driservice.domain.DataSource;
import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.exceptions.InvalidConfigurationException;

import com.google.common.util.concurrent.ListenableFuture;

public class BlobstoreHelper {

	public static ListenableFuture<Blob> getKthChunk(CloudDirectory dataset, CloudFile item, DataSource ds, AsyncBlobStore blobstore, int k, int chunks) {
		long chunkSize = item.getSize() / chunks;
		long start = k * chunkSize;
		long end = (k+1) * chunkSize - 1;
	
		// check the provided configuration parameters
		if (item.getSize() < chunks) {
			throw new InvalidConfigurationException("Item size is greater than the number N of chunks." +
					"Threshold should be >= N");
		}
	
		return blobstore.getBlob(ds.getContainer(), ds.getName(), range(start, end));
	}
	
	public static InputStream getInputStream(Blob blob) {
		return new AutoCloseInputStream(blob.getPayload().getInput());
	}
}
