package vphshare.driservice.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static vphshare.driservice.registry.AIRMetadataRegistry.DatasetCategory.ALL;
import static vphshare.driservice.registry.AIRMetadataRegistry.DatasetCategory.ONLY_MANAGED;

import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.quartz.SchedulerException;

import vphshare.driservice.domain.LogicalData;
import vphshare.driservice.domain.ManagedDataset;
import vphshare.driservice.exceptions.ResourceNotFoundException;
import vphshare.driservice.notification.NotificationService;
import vphshare.driservice.registry.MetadataRegistry;
import vphshare.driservice.scheduler.TaskSubmitter;
import vphshare.driservice.validation.DatasetValidator;

@Path("/")
public class DRIServiceImpl implements DRIService {

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DRIServiceImpl.class.getName());

	@Inject
	protected MetadataRegistry registry;
	@Inject
	protected DatasetValidator strategy;
	@Inject
	protected NotificationService notificationService;
	@Inject
	protected ExecutorService executor;
	@Inject 
	protected TaskSubmitter submitter;

	public DRIServiceImpl() {}
	
	@Override
	@GET
	@Produces(APPLICATION_JSON)
	@Path("add_to_management/{datasetID}")
	public Response addToManagement(@PathParam("datasetID") final String datasetID) throws SchedulerException {
		ManagedDataset dataset = null;
		try {
			dataset = registry.getDataset(datasetID, ONLY_MANAGED);
		
		// Not found in managed datasets
		} catch (ResourceNotFoundException e) {
			dataset = registry.getDataset(datasetID, ALL);
			submitter.submitComputeChecksumsJob(dataset);
			registry.setAsManaged(dataset);
			return Response.status(Response.Status.OK).entity("Dataset " + datasetID + " scheduled to be added to management.").build();
		}
		
		return Response.ok("Dataset " + datasetID + " is already under management").build();
	}
	
	@Override
	@GET
	@Produces(APPLICATION_JSON)
	@Path("remove_from_management/{datasetID}")
	public Response removeFromManagement(@PathParam("datasetID") final String datasetID) {
		ManagedDataset dataset = registry.getDataset(datasetID, ONLY_MANAGED);
		registry.unsetAsManaged(dataset);
		return Response.ok("Dataset " + datasetID + " removed from management.").build();
	}

	@Override
	@GET
	@Produces(APPLICATION_JSON)
	@Path("update_checksums/{datasetID}")
	public Response updateChecksums(@PathParam("datasetID") final String datasetID) throws SchedulerException {
		ManagedDataset dataset = registry.getDataset(datasetID, ONLY_MANAGED);
		submitter.submitComputeChecksumsJob(dataset);
		return Response.ok("Dataset " + datasetID + " was submitted to update its checksums.").build();
	}
	
	@Override
	@GET
	@Produces(APPLICATION_JSON)
	@Path("update_single_item/{datasetID}/{itemID}")
	public Response updateSingleItemChecksum(@PathParam("datasetID") final String datasetID,
											 @PathParam("itemID") final String itemID) throws SchedulerException {
		ManagedDataset dataset = registry.getDataset(datasetID, ONLY_MANAGED);
		LogicalData item = registry.getLogicalData(dataset, itemID);
		submitter.submitUpdateSingleItemChecksumJob(dataset, item);
		return Response.ok("Item " + itemID + " was submitted to update its checksum.").build();
	}

	@Override
	@GET
	@Produces(APPLICATION_JSON)
	@Path("validate_dataset/{datasetID}")
	public Response validateDataset(@PathParam("datasetID") final String datasetID) throws SchedulerException {
		ManagedDataset dataset = registry.getDataset(datasetID, ONLY_MANAGED);
		submitter.submitValidationJob(dataset);
		return Response.ok("Dataset " + datasetID + " was submitted to validation.").build();
	}
}
