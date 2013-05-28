package vphshare.driservice.validation;

import org.jclouds.blobstore.BlobStoreContext;

import vphshare.driservice.domain.DataSource;
import vphshare.driservice.domain.LogicalData;
import vphshare.driservice.domain.ManagedDataset;

public interface ValidationStrategy {

	String setup(ManagedDataset dataset, LogicalData item, DataSource ds, BlobStoreContext context);
	
	boolean validate(ManagedDataset dataset, LogicalData item, DataSource ds, BlobStoreContext context);
}
