package vphshare.driservice.service;

import org.apache.log4j.Logger;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.registry.MetadataRegistry;
import vphshare.driservice.task.TaskBuilder;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.concurrent.ThreadPoolExecutor;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/")
public class DRIServiceImpl implements DRIService {

    private static final Logger logger = Logger.getLogger(DRIServiceImpl.class);

    private static final Response OK_RESPONSE = Response.ok().build();
    public static final Response NOT_MODIFIED_RESPONSE = Response.notModified().build();

    @Inject
	protected MetadataRegistry registry;
    @Inject
    protected ThreadPoolExecutor executor;
    @Inject
    protected TaskBuilder taskBuilder;

    public DRIServiceImpl() {}
	
	@Override
	@POST
	@Produces(APPLICATION_JSON)
	@Path("dataset/{id}/add_to_management")
	public Response addToManagement(@PathParam("id") final String id) {
		logger.info(String.format("Invoking [/dataset/%s/add_to_management", id));
        CloudDirectory dir = registry.getCloudDirectory(id);
        if (dir.isSupervised()) {
            logger.warn(String.format("Directory already set as managed [id=%s]", id));
            return Response.notModified().build();
        } else {
            executor.execute(taskBuilder.computeChecksums(dir));
            registry.setSupervised(dir);
            return OK_RESPONSE;
        }
	}
	
	@Override
	@DELETE
	@Produces(APPLICATION_JSON)
	@Path("dataset/{id}/remove_from_management")
	public Response removeFromManagement(@PathParam("id") final String id) {
		logger.info(String.format("Invoking [/dataset/%s/remove_from_management", id));
		CloudDirectory dir = registry.getCloudDirectory(id);
        if (dir.isSupervised()) {
            registry.unsetSupervised(dir);
            return OK_RESPONSE;
        } else {
            logger.warn(String.format("Directory %s is not supervised", dir));
            return NOT_MODIFIED_RESPONSE;
        }
	}

	@Override
	@PUT
	@Produces(APPLICATION_JSON)
	@Path("dataset/{id}/update_checksums")
	public Response updateChecksums(@PathParam("id") final String id) {
		logger.info(String.format("Invoking [/dataset/%s/update_checksums", id));
		CloudDirectory dir = registry.getCloudDirectory(id);
        if (dir.isSupervised()) {
            executor.execute(taskBuilder.computeChecksums(dir));
            return OK_RESPONSE;
        } else {
            logger.warn(String.format("Directory %s is not supervised", dir));
            return NOT_MODIFIED_RESPONSE;
        }
	}
	
	@Override
	@PUT
	@Produces(APPLICATION_JSON)
	@Path("dataset/{id}/item/{fileId}/update_checksum")
	public Response updateSingleItemChecksum(@PathParam("id") final String id,
											 @PathParam("fileId") final String fileId) {
		logger.info(String.format("Invoking [/dataset/%s/item/%s/update_checksum", id, fileId));
		CloudDirectory dir = registry.getCloudDirectory(id);
        if (dir.isSupervised()) {
		    CloudFile file = registry.getCloudFile(dir, fileId);
            executor.execute(taskBuilder.updateDatasetItem(dir, file));
            return OK_RESPONSE;
        } else {
            logger.warn(String.format("Directory %s is not supervised", dir));
            return NOT_MODIFIED_RESPONSE;
        }
	}

	@Override
	@POST
	@Produces(APPLICATION_JSON)
	@Path("dataset/{id}/validate")
	public Response validateDataset(@PathParam("id") final String id) {
		logger.info(String.format("Invoking [/dataset/%s/validate]", id));
		CloudDirectory dir = registry.getCloudDirectory(id);
        if (dir.isSupervised()) {
            executor.execute(taskBuilder.validateDataset(dir));
            return OK_RESPONSE;
        } else {
            logger.warn(String.format("Directory %s is not supervised", dir));
            return NOT_MODIFIED_RESPONSE;
        }
	}
}
