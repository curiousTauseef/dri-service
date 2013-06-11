package vphshare.driservice.testing;

import static org.apache.commons.collections.CollectionUtils.find;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.exceptions.ResourceNotFoundException;
import vphshare.driservice.registry.MetadataRegistry;

public class MetadataRegistryMock implements MetadataRegistry {
	
	private List<CloudDirectory> datasets = new ArrayList<CloudDirectory>();
	private Map<CloudDirectory, List<CloudFile>> datas = new HashMap<CloudDirectory, List<CloudFile>>();

	public void setDatasets(List<CloudDirectory> datasets) {
		this.datasets = datasets;
	}
	
	public void addDataset(CloudDirectory dataset) {
		datasets.add(dataset);
	}
	
	public void setLogicalDatas(Map<CloudDirectory, List<CloudFile>> datas) {
		this.datas = datas;
	}
	
	public void addLogicalDatas(CloudDirectory dataset, List<CloudFile> items) {
		datas.put(dataset, items);
	}
	
	@Override
	public List<CloudDirectory> getCloudDirectories(boolean onlyManaged) {
		return datasets;
	}

	@Override
	public CloudDirectory getCloudDirectory(String datasetIDorName, boolean onlyManaged) {
		CloudDirectory dataset = (CloudDirectory) find(getCloudDirectories(onlyManaged), CloudDirectory.getPredicate(datasetIDorName));
		if (dataset == null) {
			if (onlyManaged)
				throw new ResourceNotFoundException("Dataset " + datasetIDorName + " not found or not set as managed.");
			else 
				throw new ResourceNotFoundException("Dataset " + datasetIDorName + " not found");
		}
		return dataset;
	}

	@Override
	public void setSupervised(CloudDirectory dataset) {
		// in this mock all datasets are managed
	}

	@Override
	public void unsetSupervised(CloudDirectory dataset) {
		// in this mock all datasets are managed
	}

	@Override
	public List<CloudFile> getCloudFiles(CloudDirectory dataset) {
		return datas.get(dataset);
	}

	@Override
	public CloudFile getCloudFile(CloudDirectory dataset, String itemIDorIdentifier) {
		CloudFile item = (CloudFile) find(getCloudFiles(dataset), CloudFile.getPredicate(itemIDorIdentifier));
		if (item == null)
			throw new ResourceNotFoundException("Logical data " + itemIDorIdentifier + " not found");
		return item;
	}

	@Override
	public void updateChecksum(CloudDirectory dataset, CloudFile item) {
		getCloudFile(dataset, item.getId()).setChecksum(item.getChecksum());
	}

	@Override
	public void updateLastValidationDate(CloudDirectory directory,
			CloudFile file) {
		// TODO Auto-generated method stub
		
	}

}
