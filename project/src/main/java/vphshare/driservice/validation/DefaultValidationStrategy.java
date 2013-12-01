package vphshare.driservice.validation;

import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;

import vphshare.driservice.domain.DataSource;
import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.exceptions.FileRetrievalException;

public class DefaultValidationStrategy implements ValidationStrategy {

	@Override
	public String setup(CloudDirectory dataset, CloudFile item, DataSource ds, BlobStoreContext context) {
		// if the size is 0, then skip
		if (item.getSize() <= 0L)
			return "empty";
		
		return computeChecksum(dataset, item, ds, context);
	}

	@Override
	public boolean validate(CloudDirectory dataset, CloudFile item, DataSource ds, BlobStoreContext context) {
		// if the size is 0, then skip
		if (item.getSize() <= 0L)
			return true;
				
		String checksum = computeChecksum(dataset, item, ds, context);
		return item.getChecksum().equals(checksum);
	}

	protected String computeChecksum(CloudDirectory directory, CloudFile file, DataSource ds, BlobStoreContext context) {
		BlobStore blobstore = context.getBlobStore();
        InputStream in = null;
		try {
			Blob blob = blobstore.getBlob(ds.getContainer(), ds.getName());
		    in = blob.getPayload().getInput();
            return DigestUtils.sha256Hex(in);
		} catch (Exception e) {
			throw new FileRetrievalException(e);
		} finally {
            if (in != null) {
                IOUtils.closeQuietly(in);
            }
        }


	}
}
