package vphshare.driservice.registry;

import java.util.List;

import vphshare.driservice.domain.LogicalData;
import vphshare.driservice.domain.ManagedDataset;
import vphshare.driservice.registry.AIRMetadataRegistry.DatasetCategory;

import com.google.inject.ImplementedBy;

@ImplementedBy(AIRMetadataRegistry.class)
public interface MetadataRegistry {

	List<ManagedDataset> getDatasets(DatasetCategory category);

	ManagedDataset getDataset(String datasetID, DatasetCategory managed);
	
	void setAsManaged(ManagedDataset dataset);

	void unsetAsManaged(ManagedDataset dataset);
	
	List<LogicalData> getLogicalDatas(ManagedDataset dataset);

	LogicalData getLogicalData(ManagedDataset dataset, String itemID);

	void updateChecksum(ManagedDataset dataset, LogicalData item);

}
