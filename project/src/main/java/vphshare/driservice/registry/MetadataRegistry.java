package vphshare.driservice.registry;

import java.util.List;

import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.registry.lobcder.LobcderMetadataRegistry;

import com.google.inject.ImplementedBy;

@ImplementedBy(LobcderMetadataRegistry.class)
public interface MetadataRegistry {

	List<CloudDirectory> getCloudDirectories(boolean onlyManaged);

	CloudDirectory getCloudDirectory(String directoryId, boolean onlyManaged);
	
	void setSupervised(CloudDirectory directory);

	void unsetSupervised(CloudDirectory directory);
	
	List<CloudFile> getCloudFiles(CloudDirectory directory);

	CloudFile getCloudFile(CloudDirectory directory, String fileId);

	void updateChecksum(CloudDirectory directory, CloudFile file);
	
	void updateLastValidationDate(CloudDirectory directory, CloudFile file);

}
