package vphshare.driservice.validation;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;

import vphshare.driservice.domain.DataSource;
import vphshare.driservice.domain.LogicalData;
import vphshare.driservice.domain.ManagedDataset;
import vphshare.driservice.exceptions.ResourceNotFoundException;

public class DefaultValidationStrategy implements ValidationStrategy {

	@Override
	public String setup(ManagedDataset dataset, LogicalData item, DataSource ds, BlobStoreContext context) {
		
		// if the size is 0, then skip
		if (item.getSize() <= 0L)
			return "";
		
		return getChecksum(dataset, item, ds, context);
	}

	@Override
	public boolean validate(ManagedDataset dataset, LogicalData item, DataSource ds, BlobStoreContext context) {
		
		// if the size is 0, then skip
		if (item.getSize() <= 0L)
			return true;
				
		String checksum = getChecksum(dataset, item, ds, context);
		boolean valid = item.getChecksum().equals(checksum);
		
		return valid;
	}

	protected String getChecksum(ManagedDataset dataset, LogicalData item, DataSource ds, BlobStoreContext context) {
		
		BlobStore blobstore = context.getBlobStore();
		Blob blob = null;
		try {
			blob = blobstore.getBlob(ds.getContainer(), ds.getName());
		} catch (RuntimeException e) {
			throw new ResourceNotFoundException("Unable to retrieve " + item.getId() + " from cloud resource");
		}
		
		if (blob == null)
			throw new ResourceNotFoundException();
		
		InputStream in = null;
		try {
			in = blob.getPayload().getInput();
		} catch (RuntimeException re) {
			throw new ResourceNotFoundException();
		}
		
		String checksum = null;
		try {
			checksum = DigestUtils.sha256Hex(in);
		} catch (IOException e) {
			checksum = "";
		} finally {
			IOUtils.closeQuietly(in);
		}
		
		return checksum;
	}
}
