package vphshare.driservice.validation;

import org.apache.log4j.Logger;
import org.jclouds.blobstore.BlobStoreContext;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.domain.DataSource;
import vphshare.driservice.notification.domain.ValidationReport;
import vphshare.driservice.providers.BlobStoreContextProvider;
import vphshare.driservice.registry.MetadataRegistry;
import vphshare.driservice.util.ValidationUtil;

import javax.inject.Inject;
import java.util.List;

import static vphshare.driservice.notification.domain.ValidationStatus.INTEGRITY_ERROR;
import static vphshare.driservice.notification.domain.ValidationStatus.UNAVAILABLE;

public class DefaultDatasetValidator implements DatasetValidator {

    private static final Logger logger = Logger.getLogger(DefaultDatasetValidator.class);

	private final MetadataRegistry registry;
	private final ValidationStrategy strategy;

    @Inject
    public DefaultDatasetValidator(MetadataRegistry registry, ValidationStrategy strategy) {
        this.registry = registry;
        this.strategy = strategy;
    }

    @Override
	public void computeChecksums(CloudDirectory directory) {
		List<CloudFile> files = registry.getCloudFiles(directory);
		logger.info(String.format("Found [%d] files", files.size()));
		computeForEachFile(directory, files);
	}

	private void computeForEachFile(CloudDirectory directory, List<CloudFile> files) {
		for (CloudFile file : files) {
			if (ValidationUtil.hasNonZeroDataSources(file)) {
                computeChecksum(directory, file, file.getDataSources().get(0));
			}
		}
	}

    private void computeChecksum(CloudDirectory directory, CloudFile file, DataSource dataSource) {
        BlobStoreContext context = null;
        try {
            context = BlobStoreContextProvider.getContext(dataSource);
            String checksum = strategy.setup(directory, file, dataSource, context);
            file.setChecksum(checksum);
            registry.updateChecksum(directory, file);

        } catch (Exception e) {
            logger.error(String.format("Error on updating file [%s] checksum value in metadata registry", file.getName()), e);
            throw new RuntimeException(e);
        } finally {
            if (context != null) {
                context.close();
            }
        }
    }

    @Override
	public ValidationReport validate(CloudDirectory directory) {
		List<CloudFile> files = registry.getCloudFiles(directory);
		ValidationReport report = new ValidationReport(directory);
        validateAllFiles(directory, files, report);
		return report;
	}

	private void validateAllFiles(CloudDirectory directory, List<CloudFile> files, ValidationReport report) {
		for (CloudFile file : files) {
            for (DataSource ds : file.getDataSources()) {
                validateFile(directory, file, ds, report);
            }
		}
	}

	private void validateFile(CloudDirectory directory, CloudFile file, DataSource ds, ValidationReport report) {
		BlobStoreContext context = null;
		try {
            context = BlobStoreContextProvider.getContext(ds);
            boolean isValid = strategy.validate(directory, file, ds, context);
            if (!isValid) {
                report.addEntryLog(file.getName(), INTEGRITY_ERROR);
            }

		} catch (Exception e) {
            logger.error(String.format("File [%s] validation failed", file.getName()), e);
            report.addEntryLog(file.getName(), UNAVAILABLE);
        } finally {
            if (context != null) {
			    context.close();
            }
		}
	}

	@Override
	public void computeChecksum(CloudDirectory directory, CloudFile file) {
        DataSource dataSource = file.getDataSources().get(0);
        BlobStoreContext context = BlobStoreContextProvider.getContext(dataSource);

        String checksum = strategy.setup(directory, file, dataSource, context);
        file.setChecksum(checksum);
        registry.updateChecksum(directory, file);
	}
}
