package vphshare.driservice.registry;

import static org.apache.commons.collections.CollectionUtils.find;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import vphshare.driservice.domain.LogicalData;
import vphshare.driservice.domain.ManagedDataset;
import vphshare.driservice.exceptions.ResourceNotFoundException;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.api.uri.UriTemplate;

public class AIRMetadataRegistry implements MetadataRegistry {
	
	public static enum DatasetCategory {
		ONLY_MANAGED {
			@Override
			public String toString() {
				return "true";
			}
		},
		ALL {
			@Override
			public String toString() {
				return "false";
			}
		};
		
		public boolean isManaged() {
			return this.equals(ONLY_MANAGED);
		}
	}
	
	private static final String GET_DATASETS_URI = "/get_data_sets";

	private static final String GET_DATA_ITEMS_URI = "/get_logical_data_for_data_set/{id}";

	private static final String UPDATE_CHECKSUM_URI = "/update_dri_checksum";
	
	private static final String SET_AS_MANAGED_URI = "/set_as_managed";

	@Inject @Named("air-config")
	protected WebResource service;
	
	@Override
	public List<ManagedDataset> getDatasets(DatasetCategory category) {
		Form form = new Form();
		form.add("only_managed", category.toString());
		
		UriTemplate uri = new UriTemplate(GET_DATASETS_URI);
		
		return service.path(uri.createURI())
				.queryParam("only_managed", category.toString())
				.get(new GenericType<List<ManagedDataset>>() {});
	}

	@Override
	public ManagedDataset getDataset(final String datasetIDorName, DatasetCategory category) {
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
		Form form = new Form();
		form.add("name", dataset.getName());
		form.add("is_managed", true);
		
		UriTemplate uri = new UriTemplate(SET_AS_MANAGED_URI);
		service.path(uri.createURI()).post(ClientResponse.class, form);
	}
	
	@Override
	public void unsetAsManaged(ManagedDataset dataset) {
		Form form = new Form();
		form.add("name", dataset.getName());
		form.add("is_managed", false);
		
		UriTemplate uri = new UriTemplate(SET_AS_MANAGED_URI);
		service.path(uri.createURI()).post(ClientResponse.class, form);
	}

	@Override
	public List<LogicalData> getLogicalDatas(ManagedDataset dataset) {
		UriTemplate uri = new UriTemplate(GET_DATA_ITEMS_URI);
		return service.path(uri.createURI(dataset.getId()))
				.get(new GenericType<List<LogicalData>>() {});
	}

	@Override
	public LogicalData getLogicalData(ManagedDataset dataset, final String itemIDorIdentifier) {
		LogicalData item = (LogicalData) find(getLogicalDatas(dataset), LogicalData.getPredicate(itemIDorIdentifier));
		if (item == null)
			throw new ResourceNotFoundException("Logical data " + itemIDorIdentifier + " not found");
		return item;
	}

	@Override
	public void updateChecksum(ManagedDataset dataset, LogicalData item) {
		Form form = new Form();
		form.add("_id", item.getId());
		form.add("dri_checksum", item.getDriChecksum());
		
		UriTemplate uri = new UriTemplate(UPDATE_CHECKSUM_URI);
		service.path(uri.createURI()).post(ClientResponse.class, form);
	}
}
