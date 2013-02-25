package vphshare.driservice.testing;

import static org.apache.commons.collections.CollectionUtils.find;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vphshare.driservice.domain.LogicalData;
import vphshare.driservice.domain.ManagedDataset;
import vphshare.driservice.exceptions.ResourceNotFoundException;
import vphshare.driservice.registry.AIRMetadataRegistry.DatasetCategory;
import vphshare.driservice.registry.MetadataRegistry;

public class MetadataRegistryMock implements MetadataRegistry {
	
	private List<ManagedDataset> datasets = new ArrayList<ManagedDataset>();
	private Map<ManagedDataset, List<LogicalData>> datas = new HashMap<ManagedDataset, List<LogicalData>>();

	public void setDatasets(List<ManagedDataset> datasets) {
		this.datasets = datasets;
	}
	
	public void addDataset(ManagedDataset dataset) {
		datasets.add(dataset);
	}
	
	public void setLogicalDatas(Map<ManagedDataset, List<LogicalData>> datas) {
		this.datas = datas;
	}
	
	public void addLogicalDatas(ManagedDataset dataset, List<LogicalData> items) {
		datas.put(dataset, items);
	}
	
	@Override
	public List<ManagedDataset> getDatasets(DatasetCategory category) {
		return datasets;
	}

	@Override
	public ManagedDataset getDataset(String datasetIDorName, DatasetCategory category) {
		ManagedDataset dataset = (ManagedDataset) find(getDatasets(category), ManagedDataset.getPredicate(datasetIDorName));
		if (dataset == null) {
			if (category.isManaged())
				throw new ResourceNotFoundException("Dataset " + datasetIDorName + " not found or not set as managed.");
			else 
				throw new ResourceNotFoundException("Dataset " + datasetIDorName + " not found");
		}
		return dataset;
	}

	@Override
	public void setAsManaged(ManagedDataset dataset) {
		// in this mock all datasets are managed
	}

	@Override
	public void unsetAsManaged(ManagedDataset dataset) {
		// in this mock all datasets are managed
	}

	@Override
	public List<LogicalData> getLogicalDatas(ManagedDataset dataset) {
		return datas.get(dataset);
	}

	@Override
	public LogicalData getLogicalData(ManagedDataset dataset, String itemIDorIdentifier) {
		LogicalData item = (LogicalData) find(getLogicalDatas(dataset), LogicalData.getPredicate(itemIDorIdentifier));
		if (item == null)
			throw new ResourceNotFoundException("Logical data " + itemIDorIdentifier + " not found");
		return item;
	}

	@Override
	public void updateChecksum(ManagedDataset dataset, LogicalData item) {
		getLogicalData(dataset, item.getId()).setDriChecksum(item.getDriChecksum());
	}

}
