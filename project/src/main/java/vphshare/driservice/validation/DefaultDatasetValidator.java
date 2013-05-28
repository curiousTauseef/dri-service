package vphshare.driservice.validation;

import static vphshare.driservice.notification.domain.ValidationStatus.INTEGRITY_ERROR;
import static vphshare.driservice.notification.domain.ValidationStatus.UNAVAILABLE;
import static vphshare.driservice.notification.domain.ValidationStatus.VALID;

import java.util.List;
import java.util.logging.Level;
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
		LOG.log(Level.INFO, "Found " + items.size() + " items!");
		computeChecksumsForEachLogicalData(dataset, report, items);
		return report;
	}

	private void computeChecksumsForEachLogicalData(ManagedDataset dataset, DatasetReport report, List<LogicalData> items) {
		for (LogicalData item : items) {
			if (item.getDataSources().size() > 0) {
				BlobStoreContext context = null;
				try {
					context = BlobStoreContextProvider.getContext(item.getDataSources().get(0));
					item.setChecksum(strategy.setup(dataset, item, item.getDataSources().get(0), context));
					registry.updateChecksum(dataset, item);
				
				} catch (ResourceNotFoundException e) {
					report.addEntryLog(item.getName(), UNAVAILABLE);
				} finally {
					context.close();
				}
			}
		}
	}

	@Override
	public DatasetReport validate(ManagedDataset dataset) {
		List<LogicalData> items = registry.getLogicalDatas(dataset);
		DatasetReport report = new DatasetReport(dataset);
		report.setStatus(VALID);

		try {
			validateDatasetItems(dataset, items, report);

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

	private void validateDatasetItems(ManagedDataset dataset, List<LogicalData> items, DatasetReport report) {
		for (LogicalData item : items) {
			validateLogicalData(dataset, item, report);
		}
	}

	private void validateLogicalData(ManagedDataset dataset, LogicalData item, DatasetReport report) {
		for (DataSource ds : item.getDataSources()) {
			validateLogicalDataOnDataSource(dataset, item, ds, report);
		}
	}
	
	private void validateLogicalDataOnDataSource(ManagedDataset dataset, LogicalData item, DataSource ds, DatasetReport report) {
		BlobStoreContext context = BlobStoreContextProvider.getContext(ds);
		try {
			ValidationStatus status = validateLogicalData(dataset, item, ds, context);
			if (!status.isValid()) {
				report.addEntryLog(item.getName(), status);
			}

		} catch (ResourceNotFoundException e) {
			report.addEntryLog(item.getName(), UNAVAILABLE);
		} finally {
			context.close();
		}
	}

	protected ValidationStatus validateLogicalData(ManagedDataset dataset, LogicalData item, DataSource ds, BlobStoreContext context) {
		return strategy.validate(dataset, item, ds, context) ? VALID : INTEGRITY_ERROR;
	}

	@Override
	public DatasetReport computeChecksum(ManagedDataset dataset, LogicalData item) {
		DatasetReport report = new DatasetReport(dataset);
		BlobStoreContext context = BlobStoreContextProvider.getContext(item.getDataSources().get(0));
		
		try {
			item.setChecksum(strategy.setup(dataset, item, item.getDataSources().get(0), context));
			registry.updateChecksum(dataset, item);
		
		} catch (ResourceNotFoundException e) {
			report.addEntryLog(item.getName(), UNAVAILABLE);
		}
		
		return report;
	}
}
