package vphshare.driservice.validation;

import vphshare.driservice.domain.LogicalData;
import vphshare.driservice.domain.ManagedDataset;
import vphshare.driservice.notification.domain.DatasetReport;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultDatasetValidator.class)
public interface DatasetValidator {
	
	DatasetReport computeChecksums(ManagedDataset dataset);
	
	DatasetReport computeChecksum(ManagedDataset dataset, LogicalData item);

	DatasetReport validate(ManagedDataset dataset);
}
