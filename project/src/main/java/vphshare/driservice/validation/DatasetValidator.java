package vphshare.driservice.validation;

import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.notification.domain.DatasetReport;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultDatasetValidator.class)
public interface DatasetValidator {
	
	DatasetReport computeChecksums(CloudDirectory dataset);
	
	DatasetReport computeChecksum(CloudDirectory dataset, CloudFile item);

	DatasetReport validate(CloudDirectory dataset);
}
