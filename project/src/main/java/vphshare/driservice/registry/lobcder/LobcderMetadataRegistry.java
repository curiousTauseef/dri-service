package vphshare.driservice.registry.lobcder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

import vphshare.driservice.domain.DataSource;
import vphshare.driservice.domain.LogicalData;
import vphshare.driservice.domain.ManagedDataset;
import vphshare.driservice.exceptions.ResourceNotFoundException;
import vphshare.driservice.registry.AIRMetadataRegistry.DatasetCategory;
import vphshare.driservice.registry.MetadataRegistry;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.uri.UriTemplate;

public class LobcderMetadataRegistry implements MetadataRegistry {
	
	protected WebResource service;

	@Inject 
	public LobcderMetadataRegistry(@Named("lobcder-config") WebResource service) {
		this.service = service;
	}
	
	@Override
	public List<ManagedDataset> getDatasets(DatasetCategory category) {
		UriTemplate uri = new UriTemplate("/items/query");
		List<WrappedLogicalData> wrappeds = null;
		if (category.equals(DatasetCategory.ALL)) {
			wrappeds = service
				.path(uri.createURI())
				.get(new GenericType<List<WrappedLogicalData>>() {});
		} else {
			wrappeds = service
				.queryParam("isSupervised", "true")
				.path(uri.createURI())
				.get(new GenericType<List<WrappedLogicalData>>() {});
		}
		
		List<WrappedLogicalData> filtered = new ArrayList<WrappedLogicalData>();
		for (WrappedLogicalData wrapped : wrappeds) {
			if ("logical.folder".equals(wrapped.getLogicalData().getType()))
				filtered.add(wrapped);
		}
		
		List<ManagedDataset> datasets = new ArrayList<ManagedDataset>();
		for (WrappedLogicalData wrapped : filtered) {
			ManagedDataset dataset = new ManagedDataset();
			dataset.setId(Long.toString(wrapped.getLogicalData().getUid()));
			dataset.setName(wrapped.getPath());
			datasets.add(dataset);
		}
		
		return datasets;
	}

	@Override
	public ManagedDataset getDataset(String datasetID, DatasetCategory category) {
		UriTemplate uri = new UriTemplate("/item/query/{uid}");
		WrappedLogicalData wld = service
			.path(uri.createURI(datasetID))
			.get(WrappedLogicalData.class);
		
		if (! "logical.folder".equals(wld.getLogicalData().getType()))
			throw new ResourceNotFoundException("Dataset " + datasetID + " does not exist");
		
		ManagedDataset dataset = new ManagedDataset();
		dataset.setId(Long.toString(wld.getLogicalData().getUid()));
		dataset.setName(wld.getPath());
		
		if (category.equals(DatasetCategory.ALL)) {
			return dataset;
		} else {
			if (wld.getLogicalData().isSupervised())
				return dataset;
			else
				throw new ResourceNotFoundException("Dataset " + datasetID + " does not exist or not set as supervised");
		}
	}

	@Override
	public void setAsManaged(ManagedDataset dataset) {
		UriTemplate uri = new UriTemplate("/item/dri/{uid}/supervised/{flag}/");
		service
			.path(uri.createURI(dataset.getId(), "true"))
			.put();
	}

	@Override
	public void unsetAsManaged(ManagedDataset dataset) {
		UriTemplate uri = new UriTemplate("/item/dri/{uid}/supervised/{flag}/");
		service
			.path(uri.createURI(dataset.getId(), "false"))
			.put();
	}

	@Override
	public List<LogicalData> getLogicalDatas(ManagedDataset dataset) {
		UriTemplate uri = new UriTemplate("/items/query");
		List<WrappedLogicalData> wrappeds = service
				.queryParam("path", dataset.getName())
				.path(uri.createURI())
				.get(new GenericType<List<WrappedLogicalData>>() {});
		
		List<WrappedLogicalData> filtered = new ArrayList<WrappedLogicalData>();
		for (WrappedLogicalData wrapped : wrappeds) {
			if ("logical.file".equals(wrapped.getLogicalData().getType())
					&& wrapped.getPath().equals(dataset.getName() + "/" + wrapped.getLogicalData().getName()))
				filtered.add(wrapped);
		}
			
		return Lists.transform(filtered, new Function<WrappedLogicalData, LogicalData>() {

			@Override
			public LogicalData apply(@Nullable WrappedLogicalData input) {
				LogicalData ld = new LogicalData();
				ld.setId(Long.toString(input.getLogicalData().getUid()));
				ld.setName(input.getLogicalData().getName());
				ld.setPath(input.getPath());
				ld.setSize(input.getLogicalData().getLength());
				ld.setChecksum(input.getLogicalData().getChecksum());
				ld.setLastValidationDate(Long.toString(input.getLogicalData().getLastValidationDate()));
				ld.setDataSources(Arrays.asList(input.getDataSource()));
				
				for (DataSource ds : ld.getDataSources()) {
					ds.setContainer("LOBCDER-REPLICA-vTEST");
				}
				
				return ld;
			}
		});
	}

	@Override
	public LogicalData getLogicalData(ManagedDataset dataset, String itemID) {
		UriTemplate uri = new UriTemplate("/item/query/{uid}");
		WrappedLogicalData wld = service
			.path(uri.createURI(itemID))
			.get(new GenericType<WrappedLogicalData>() {});
		
		if (!"logical.file".equals(wld.getLogicalData().getType()))
			throw new ResourceNotFoundException("Logical data " + itemID + " does not exist");
		
		LogicalData ld = new LogicalData();
		ld.setId(Long.toString(wld.getLogicalData().getUid()));
		ld.setName(wld.getLogicalData().getName());
		ld.setPath(wld.getPath());
		ld.setSize(wld.getLogicalData().getLength());
		ld.setChecksum(wld.getLogicalData().getChecksum());
		ld.setLastValidationDate(Long.toString(wld.getLogicalData().getLastValidationDate()));
		ld.setDataSources(Arrays.asList(wld.getDataSource()));
		
		for (DataSource ds : ld.getDataSources()) {
			ds.setContainer("LOBCDER-REPLICA-vTEST");
		}
		
		return ld;
	}

	@Override
	public void updateChecksum(ManagedDataset dataset, LogicalData item) {
		UriTemplate uri = new UriTemplate("/item/dri/{uid}/checksum/{checksum}/");
		service
			.path(uri.createURI(item.getId(), item.getChecksum()))
			.put();

	}

}
