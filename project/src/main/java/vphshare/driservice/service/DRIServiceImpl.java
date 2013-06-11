package vphshare.driservice.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.exceptions.ResourceNotFoundException;
import vphshare.driservice.registry.MetadataRegistry;
import vphshare.driservice.scheduler.TaskSubmitter;

@Path("/")
public class DRIServiceImpl implements DRIService {

	private static final Logger logger = Logger.getLogger(DRIService.class);

	@Inject
	protected MetadataRegistry registry;
	@Inject 
	protected TaskSubmitter submitter;

	public DRIServiceImpl() {}
	
	@Override
	@GET
	@Produces(APPLICATION_JSON)
	@Path("add_to_management/{directoryId}")
	public Response addToManagement(@PathParam("directoryId") final String directoryId) throws SchedulerException {
		logger.info("Invoking [/add_to_management/" + directoryId + "]");
		CloudDirectory dir = null;
		try {
			dir = registry.getCloudDirectory(directoryId, true);
		
		// Not found in managed datasets
		} catch (ResourceNotFoundException e) {
			dir = registry.getCloudDirectory(directoryId, false);
			submitter.submitComputeChecksumsJob(dir);
			registry.setSupervised(dir);
			return Response.ok("Dataset " + directoryId + " scheduled to be added to management.").build();
		}
		
		logger.warn("Directory already set as managed [id=" + directoryId + "]");
		return Response.ok("Dataset " + directoryId + " is already under management").build();
	}
	
	@Override
	@GET
	@Produces(APPLICATION_JSON)
	@Path("remove_from_management/{directoryId}")
	public Response removeFromManagement(@PathParam("directoryId") final String directoryId) {
		logger.info("Invoking [/remove_from_management/" + directoryId + "]");
		CloudDirectory dir = registry.getCloudDirectory(directoryId, true);
		registry.unsetSupervised(dir);
		return Response.ok("Dataset " + directoryId + " removed from management.").build();
	}

	@Override
	@GET
	@Produces(APPLICATION_JSON)
	@Path("update_checksums/{directoryId}")
	public Response updateChecksums(@PathParam("directoryId") final String directoryId) throws SchedulerException {
		logger.info("Invoking [/update_checksums/" + directoryId + "]");
		CloudDirectory dir = registry.getCloudDirectory(directoryId, true);
		submitter.submitComputeChecksumsJob(dir);
		return Response.ok("Dataset " + directoryId + " was submitted to update its checksums.").build();
	}
	
	@Override
	@GET
	@Produces(APPLICATION_JSON)
	@Path("update_single_item/{directoryId}/{fileId}")
	public Response updateSingleItemChecksum(@PathParam("directoryId") final String directoryId,
											 @PathParam("fileId") final String fileId) throws SchedulerException {
		logger.info("Invoking [/update_single_item/" + directoryId + "/" + fileId + "]");
		CloudDirectory dir = registry.getCloudDirectory(directoryId, true);
		CloudFile file = registry.getCloudFile(dir, fileId);
		submitter.submitUpdateSingleItemChecksumJob(dir, file);
		return Response.ok("Item " + fileId + " was submitted to update its checksum.").build();
	}

	@Override
	@GET
	@Produces(APPLICATION_JSON)
	@Path("validate_dataset/{directoryId}")
	public Response validateDataset(@PathParam("directoryId") final String directoryId) throws SchedulerException {
		logger.info("Invoking [/validate_dataset/" + directoryId + "]");
		CloudDirectory dir = registry.getCloudDirectory(directoryId, true);
		submitter.submitValidationJob(dir);
		return Response.ok("Dataset " + directoryId + " was submitted to validation.").build();
	}
}
