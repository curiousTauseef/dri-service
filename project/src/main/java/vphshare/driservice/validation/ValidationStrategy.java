package vphshare.driservice.validation;

import org.jclouds.blobstore.BlobStoreContext;

import vphshare.driservice.domain.DataSource;
import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.domain.CloudDirectory;

public interface ValidationStrategy {

	String setup(CloudDirectory dataset, CloudFile item, DataSource ds, BlobStoreContext context);
	
	boolean validate(CloudDirectory dataset, CloudFile item, DataSource ds, BlobStoreContext context);
}
