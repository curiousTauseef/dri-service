package vphshare.driservice.registry;

import static org.apache.commons.collections.CollectionUtils.find;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.exceptions.ResourceNotFoundException;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.api.uri.UriTemplate;

@Deprecated
public class AIRMetadataRegistry implements MetadataRegistry {
	
	private static final String SET_AS_MANAGED_URI = "/set_as_managed";

	@Inject @Named("air-config")
	protected WebResource service;
	
	@Override
	public List<CloudDirectory> getCloudDirectories(boolean onlyManaged) {
		UriTemplate uri = new UriTemplate("/get_data_sets");
		
		return service.path(uri.createURI())
				.queryParam("only_managed", Boolean.toString(onlyManaged))
				.get(new GenericType<List<CloudDirectory>>() {});
	}

	@Override
	public CloudDirectory getCloudDirectory(final String datasetIDorName, boolean onlyManaged) {
		List<CloudDirectory> allDirectories = getCloudDirectories(onlyManaged);
		CloudDirectory dataset = (CloudDirectory) find(allDirectories, CloudDirectory.getPredicate(datasetIDorName));
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
		Form form = new Form();
		form.add("name", dataset.getName());
		form.add("is_managed", true);
		
		UriTemplate uri = new UriTemplate(SET_AS_MANAGED_URI);
		service.path(uri.createURI()).post(ClientResponse.class, form);
	}
	
	@Override
	public void unsetSupervised(CloudDirectory dataset) {
		Form form = new Form();
		form.add("name", dataset.getName());
		form.add("is_managed", false);
		
		UriTemplate uri = new UriTemplate(SET_AS_MANAGED_URI);
		service.path(uri.createURI()).post(ClientResponse.class, form);
	}

	@Override
	public List<CloudFile> getCloudFiles(CloudDirectory dataset) {
		UriTemplate uri = new UriTemplate("/get_logical_data_for_data_set/{id}");
		return service.path(uri.createURI(dataset.getId()))
				.get(new GenericType<List<CloudFile>>() {});
	}

	@Override
	public CloudFile getCloudFile(CloudDirectory dataset, final String itemIDorIdentifier) {
		CloudFile item = (CloudFile) find(getCloudFiles(dataset), CloudFile.getPredicate(itemIDorIdentifier));
		if (item == null)
			throw new ResourceNotFoundException("Logical data " + itemIDorIdentifier + " not found");
		return item;
	}

	@Override
	public void updateChecksum(CloudDirectory dataset, CloudFile item) {
		Form form = new Form();
		form.add("_id", item.getId());
		form.add("dri_checksum", item.getChecksum());
		
		UriTemplate uri = new UriTemplate("/update_dri_checksum");
		service.path(uri.createURI()).post(ClientResponse.class, form);
	}

	@Override
	public void updateLastValidationDate(CloudDirectory directory, CloudFile file) {
		// No op
	}
}
