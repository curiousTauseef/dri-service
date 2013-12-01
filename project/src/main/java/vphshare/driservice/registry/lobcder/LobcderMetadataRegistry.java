package vphshare.driservice.registry.lobcder;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.uri.UriTemplate;
import org.apache.log4j.Logger;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.domain.DataSource;
import vphshare.driservice.exceptions.ResourceNotFoundException;
import vphshare.driservice.registry.MetadataRegistry;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;

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

        return FluentIterable
                .from(wrappeds)
                .filter(IsDirectoryPredicate.INSTANCE)
                .transform(LobcderCloudDirectoryConverter.INSTANCE)
                .toList();
	}

	@Override
	public CloudDirectory getCloudDirectory(String directoryId) {
		logger.debug("Getting directory [id=" + directoryId + "] from LOBCDER");
		String getCloudDirPath = new UriTemplate(QUERY_ITEMS_URI).createURI(directoryId);
		logger.debug("Using URI: " + service.getURI() + getCloudDirPath);
		WrappedLogicalData wld = service
			    .path(getCloudDirPath)
			    .get(WrappedLogicalData.class);

		if (!wld.getLogicalData().isDirectory())
			throw new ResourceNotFoundException("Directory " + directoryId + " does not exist");
		
		return LobcderCloudDirectoryConverter.INSTANCE.apply(wld);
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
        logger.debug(String.format("Lobcder service returned [%d] files for %s",
                wrappeds.size(),
                directory));

        List<CloudFile> files =  FluentIterable
                .from(wrappeds)
                .filter(
                        and(
                            not(IsDirectoryPredicate.INSTANCE),
                            new IsDirectChild(directory)))
                .transform(LobcderCloudFileConverter.INSTANCE)
                .toList();
        logger.debug(String.format("[%d] files after filtering %s", files.size(), directory));
        return files;
		
    }
	@Override
	public CloudFile getCloudFile(CloudDirectory directory, String fileId) {
		logger.debug("Getting cloud file [id=" + fileId + "] for directory [id=" + directory.getId() + "]");
		String getCloudFilePath = new UriTemplate(QUERY_ITEMS_URI).createURI(fileId);
		logger.debug("Using URI: " + service.getURI() + getCloudFilePath);
		WrappedLogicalData wld = service
			.path(getCloudFilePath)
			.get(new GenericType<WrappedLogicalData>() {});
		
		if (!wld.getLogicalData().isFile())
			throw new ResourceNotFoundException("Cloud file " + fileId + " does not exist");

		return LobcderCloudFileConverter.INSTANCE.apply(wld);
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

    public static class IsDirectoryPredicate implements Predicate<WrappedLogicalData> {

        public static final IsDirectoryPredicate INSTANCE = new IsDirectoryPredicate();

        private IsDirectoryPredicate() {}

        @Override
        public boolean apply(WrappedLogicalData wld) {
            return wld.getLogicalData().isDirectory();
        }
    }

    public static class IsDirectChild implements Predicate<WrappedLogicalData> {
        private final CloudDirectory dir;

        public IsDirectChild(CloudDirectory dir) {
            this.dir = dir;
        }

        @Override
        public boolean apply(WrappedLogicalData wld) {
            if (dir.getName().equals("/")) {
                return wld.getPath().equals("/".concat(wld.getLogicalData().getName()));
            } else {
                return wld.getPath().equals(dir.getName().concat("/").concat(wld.getLogicalData().getName()));
            }
        }
    }

    public static class LobcderCloudDirectoryConverter implements Function<WrappedLogicalData, CloudDirectory> {

        public static final LobcderCloudDirectoryConverter INSTANCE = new LobcderCloudDirectoryConverter();

        private LobcderCloudDirectoryConverter() {}

        @Override
        public CloudDirectory apply(WrappedLogicalData wld) {
            LogicalData ld = wld.getLogicalData();
            checkArgument(ld.isDirectory(), "Logical data has to be a directory");

            return new CloudDirectory(
                    Long.toString(ld.getUid()),
                    wld.getPath(),
                    ld.getOwner(),
                    ld.isSupervised()
            );
        }
    }

    public static class LobcderCloudFileConverter implements Function<WrappedLogicalData, CloudFile> {

        public static final LobcderCloudFileConverter INSTANCE = new LobcderCloudFileConverter();

        private LobcderCloudFileConverter() {}

        @Override
        public CloudFile apply(WrappedLogicalData wld) {
            LogicalData ld = wld.getLogicalData();
            checkArgument(ld.isFile(), "Logical data has to be a file");

            CloudFile file = new CloudFile(
                    Long.toString(ld.getUid()),
                    ld.getName(),
                    wld.getPath(),
                    ld.getLength()
            );

            file.setChecksum(ld.getChecksum());
            file.setLastValidationDate(Long.toString(ld.getLastValidationDate()));
            file.setDataSources(wld.getDataSources());

            // TODO container is hardcoded in LOBCDER, we have to hardcode too, refactor it
            for (DataSource ds : file.getDataSources()) {
                ds.setContainer("LOBCDER-REPLICA-vTEST");
            }

            return file;
        }
    }

}
