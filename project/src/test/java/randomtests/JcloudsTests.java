package randomtests;

import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.BlobStoreContextFactory;
import org.jclouds.blobstore.domain.StorageMetadata;


public class JcloudsTests {
	public static void main(String[] args) {
		BlobStoreContextFactory contextFactory = new BlobStoreContextFactory();
		BlobStoreContext context = contextFactory.createContext("aws-s3", 
																"user",
																"password",
																null);
		BlobStore blobstore = context.getBlobStore();
		StorageMetadata metadata;
	}
}
