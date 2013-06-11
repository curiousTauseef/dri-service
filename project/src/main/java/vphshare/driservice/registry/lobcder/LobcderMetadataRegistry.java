package vphshare.driservice.registry.lobcder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.domain.DataSource;
import vphshare.driservice.exceptions.ResourceNotFoundException;
import vphshare.driservice.registry.MetadataRegistry;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.uri.UriTemplate;

public class LobcderMetadataRegistry implements MetadataRegistry {
	
	private static final Logger logger = Logger.getLogger(LobcderMetadataRegistry.class);
	
	private static final String QUERY_ITEMS_URI = "/item/query/{uid}";
	private static final String SET_DIR_SUPERVISED_URI = "/item/dri/{uid}/supervised/{flag}/";
	
	protected WebResource service;

	@Inject 
	public LobcderMetadataRegistry(@Named("lobcder-config") WebResource service) {
		this.service = service;
	}
	
	@Override
	public List<CloudDirectory> getCloudDirectories(boolean onlyManaged) {
		logger.debug("Getting all cloud directories from LOBCDER [onlyManaged=" + Boolean.toString(onlyManaged) + "]");
		String getCloudDirsPath = new UriTemplate("/items/query").createURI();
		logger.debug("Using URI: " + service.getURI() + getCloudDirsPath);
		
		List<WrappedLogicalData> wrappeds = service
				.queryParam("isSupervised", Boolean.toString(onlyManaged))
				.path(getCloudDirsPath)
				.get(new GenericType<List<WrappedLogicalData>>() {});
		
		List<WrappedLogicalData> filtered = new ArrayList<WrappedLogicalData>();
		for (WrappedLogicalData wrapped : wrappeds) {
			if ("logical.folder".equals(wrapped.getLogicalData().getType()))
				filtered.add(wrapped);
		}
		
		List<CloudDirectory> datasets = new ArrayList<CloudDirectory>();
		for (WrappedLogicalData wrapped : filtered) {
			CloudDirectory dir = new CloudDirectory();
			dir.setId(Long.toString(wrapped.getLogicalData().getUid()));
			dir.setName(wrapped.getPath());
			datasets.add(dir);
		}
		
		logger.info("Query returned " + datasets.size() + " cloud directory entries");
		return datasets;
	}

	@Override
	public CloudDirectory getCloudDirectory(String directoryId, boolean onlyManaged) {
		logger.debug("Getting directory [id=" + directoryId + "] from LOBCDER");
		String getCloudDirPath = new UriTemplate(QUERY_ITEMS_URI).createURI(directoryId);
		logger.debug("Using URI: " + service.getURI() + getCloudDirPath);
		WrappedLogicalData wld = service
			.path(getCloudDirPath)
			.get(WrappedLogicalData.class);
		
		if (! "logical.folder".equals(wld.getLogicalData().getType()))
			throw new ResourceNotFoundException("Directory " + directoryId + " does not exist");
		
		CloudDirectory dir = new CloudDirectory();
		dir.setId(Long.toString(wld.getLogicalData().getUid()));
		dir.setName(wld.getPath());
		
		if (onlyManaged) {
			if (wld.getLogicalData().isSupervised())
				return dir;
			else
				throw new ResourceNotFoundException("Directory " + directoryId + " does not exist or not set as supervised");
		} else {
			return dir;
		}
	}

	@Override
	public void setSupervised(CloudDirectory directory) {
		logger.debug("Setting directory [id=" + directory.getId() + "] as supervised");
		String setAsManagedPath = new UriTemplate(SET_DIR_SUPERVISED_URI).createURI(directory.getId(), "true");
		logger.debug("Using URI: " + service.getURI() + setAsManagedPath);
		service
			.path(setAsManagedPath)
			.put();
	}

	@Override
	public void unsetSupervised(CloudDirectory directory) {
		logger.debug("Unsetting directory [id=" + directory.getId() + "] as supervised");
		String unsetAsManagedPath = new UriTemplate(SET_DIR_SUPERVISED_URI).createURI(directory.getId(), "false");
		logger.debug("Using URI: " + service.getURI() + unsetAsManagedPath);
		service
			.path(unsetAsManagedPath)
			.put();
	}

	@Override
	public List<CloudFile> getCloudFiles(CloudDirectory directory) {
		logger.debug("Getting all cloud files for directory [id=" + directory.getId() + "]");
		String getCloudFilesPath = new UriTemplate("/items/query").createURI();
		logger.debug("Using URI: " + service.getURI() + getCloudFilesPath);
		List<WrappedLogicalData> wrappeds = service
				.queryParam("path", directory.getName())
				.path(getCloudFilesPath)
				.get(new GenericType<List<WrappedLogicalData>>() {});
		
		List<WrappedLogicalData> filtered = new ArrayList<WrappedLogicalData>();
		for (WrappedLogicalData wrapped : wrappeds) {
			if ("logical.file".equals(wrapped.getLogicalData().getType())
					&& wrapped.getPath().equals(directory.getName() + "/" + wrapped.getLogicalData().getName()))
				filtered.add(wrapped);
		}
		
		logger.debug("Query returned " + filtered.size() + " cloud files");
		return Lists.transform(filtered, new Function<WrappedLogicalData, CloudFile>() {

			@Override
			public CloudFile apply(@Nullable WrappedLogicalData input) {
				CloudFile ld = new CloudFile();
				ld.setId(Long.toString(input.getLogicalData().getUid()));
				ld.setName(input.getLogicalData().getName());
				ld.setPath(input.getPath());
				ld.setSize(input.getLogicalData().getLength());
				ld.setChecksum(input.getLogicalData().getChecksum());
				ld.setLastValidationDate(Long.toString(input.getLogicalData().getLastValidationDate()));
				ld.setDataSources(Arrays.asList(input.getDataSource()));
				
				// TODO container is hardcoded in LOBCDER, we have to hardcode too, refactor it
				for (DataSource ds : ld.getDataSources()) {
					ds.setContainer("LOBCDER-REPLICA-vTEST");
				}
				
				return ld;
			}
		});
	}

	@Override
	public CloudFile getCloudFile(CloudDirectory directory, String fileId) {
		logger.debug("Getting cloud file [id=" + fileId + "] for directory [id=" + directory.getId() + "]");
		String getCloudFilePath = new UriTemplate(QUERY_ITEMS_URI).createURI(fileId);
		logger.debug("Using URI: " + service.getURI() + getCloudFilePath);
		WrappedLogicalData wld = service
			.path(getCloudFilePath)
			.get(new GenericType<WrappedLogicalData>() {});
		
		if (!"logical.file".equals(wld.getLogicalData().getType()))
			throw new ResourceNotFoundException("Cloud file " + fileId + " does not exist");
		
		CloudFile ld = new CloudFile();
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
	public void updateChecksum(CloudDirectory directory, CloudFile file) {
		logger.debug("Updating checksum for file [id=" + file.getId() + "] for directory [id=" + directory.getId() + "]");
		String updateChecksumPath = new UriTemplate("/item/dri/{uid}/checksum/{checksum}/")
			.createURI(file.getId(), file.getChecksum());
		logger.debug("Using URI: " + service.getURI() + updateChecksumPath);
		service
			.path(updateChecksumPath)
			.put();

	}

	@Override
	public void updateLastValidationDate(CloudDirectory directory, CloudFile file) {
		logger.debug("Updating last validation date for file [id=" + file.getId() + "] for directory [id=" + directory.getId() + "]");
		String now = Long.toString(new Date().getTime());
		String updateValidationDatePath = new UriTemplate("/item/dri/{uid}/lastValidationDate/{date}")
			.createURI(file.getId(), now);
		logger.debug("Using URI: " + service.getURI() + updateValidationDatePath);
		service
			.path(updateValidationDatePath)
			.put();
	}

}
