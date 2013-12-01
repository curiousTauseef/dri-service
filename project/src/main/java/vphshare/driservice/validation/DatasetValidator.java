package vphshare.driservice.validation;

import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.notification.domain.ValidationReport;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultDatasetValidator.class)
public interface DatasetValidator {
	
	void computeChecksums(CloudDirectory directory);
	
	void computeChecksum(CloudDirectory directory, CloudFile file);

	ValidationReport validate(CloudDirectory directory);
}
