package vphshare.driservice.validation;

import static vphshare.driservice.notification.domain.ValidationStatus.INTEGRITY_ERROR;
import static vphshare.driservice.notification.domain.ValidationStatus.UNAVAILABLE;
import static vphshare.driservice.notification.domain.ValidationStatus.VALID;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.ContainerNotFoundException;

import vphshare.driservice.domain.DataSource;
import vphshare.driservice.domain.LogicalData;
import vphshare.driservice.domain.ManagedDataset;
import vphshare.driservice.exceptions.ResourceNotFoundException;
import vphshare.driservice.notification.domain.DatasetReport;
import vphshare.driservice.notification.domain.ValidationStatus;
import vphshare.driservice.providers.BlobStoreContextProvider;
import vphshare.driservice.registry.MetadataRegistry;

public class DefaultDatasetValidator implements DatasetValidator {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultDatasetValidator.class.getName());

	@Inject
	protected MetadataRegistry registry;
	
	@Inject 
	ValidationStrategy strategy;

	public DefaultDatasetValidator() {}

	@Override
	public DatasetReport computeChecksums(ManagedDataset dataset) {
		DatasetReport report = new DatasetReport(dataset);
		report.setStatus(VALID);
		List<LogicalData> items = registry.getLogicalDatas(dataset);

		BlobStoreContext context = BlobStoreContextProvider.getContext(dataset.getDataSources().get(0));
		try {
			computeChecksumsForEachLogicalData(dataset, report, items, context);
			
		} finally {
			context.close();
		}
		
		return report;
	}

	private void computeChecksumsForEachLogicalData(ManagedDataset dataset,
			DatasetReport report, List<LogicalData> items, BlobStoreContext context) {
		for (LogicalData item : items) {
			try {
				item.setDriChecksum(strategy.setup(dataset, item, context));
				registry.updateChecksum(dataset, item);
			
			} catch (ResourceNotFoundException e) {
				report.addEntryLog(item.getIdentifier(), UNAVAILABLE);
			}
		}
	}

	@Override
	public DatasetReport validate(ManagedDataset dataset) {
		List<LogicalData> items = registry.getLogicalDatas(dataset);
		DatasetReport report = new DatasetReport(dataset);
		report.setStatus(VALID);

		try {
			validateDatasetForEachDataSource(dataset, items, report);

		// TODO Swift doesn't throw this exception when container is deleted...
		} catch (ContainerNotFoundException e) {
			report.setStatus(UNAVAILABLE);
			report.setTitle("Dataset doesn't exist");
		}

		if (report.isValid()) {
			report.setTitle("Dataset valid");
		} else {
			report.setTitle("Dataset invalid");
		}
		
		return report;
	}

	private void validateDatasetForEachDataSource(ManagedDataset dataset,
			List<LogicalData> items, DatasetReport report) {
		for (DataSource dataSource : dataset.getDataSources()) {
			BlobStoreContext context = BlobStoreContextProvider.getContext(dataSource);
			try {
				validateAllLogicalDatas(dataset, items, report, context);
				
			} finally {
				context.close();
			}
		}
	}

	private void validateAllLogicalDatas(ManagedDataset dataset,
			List<LogicalData> items, DatasetReport report, BlobStoreContext context) {
		for (LogicalData item : items) {
			try {
				ValidationStatus status = validateLogicalData(dataset, item, context);
				if (!status.isValid()) {
					report.addEntryLog(item.getIdentifier(), status);
				}

			} catch (ResourceNotFoundException e) {
				report.addEntryLog(item.getIdentifier(), UNAVAILABLE);
			}
		}
	}

	protected ValidationStatus validateLogicalData(ManagedDataset dataset, LogicalData item, BlobStoreContext context) {
		return strategy.validate(dataset, item, context) ? VALID : INTEGRITY_ERROR;
	}

	@Override
	public DatasetReport computeChecksum(ManagedDataset dataset, LogicalData item) {
		DatasetReport report = new DatasetReport(dataset);
		BlobStoreContext context = BlobStoreContextProvider.getContext(dataset.getDataSources().get(0));
		
		try {
			item.setDriChecksum(strategy.setup(dataset, item, context));
			registry.updateChecksum(dataset, item);
		
		} catch (ResourceNotFoundException e) {
			report.addEntryLog(item.getIdentifier(), UNAVAILABLE);
		}
		
		return report;
	}
}
