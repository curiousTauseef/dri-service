package vphshare.driservice.testing;

import static com.google.inject.name.Names.bindProperties;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import vphshare.driservice.domain.DataSource;
import vphshare.driservice.domain.ManagedDataset;
import vphshare.driservice.notification.domain.DatasetReport;
import vphshare.driservice.providers.PropertiesProvider;
import vphshare.driservice.registry.MetadataRegistry;
import vphshare.driservice.testing.prepare.DataSourcePreparation;
import vphshare.driservice.testing.prepare.DatasetGenericBuilder;
import vphshare.driservice.testing.prepare.RealWorldDataDatasetBuilder;
import vphshare.driservice.testing.prepare.SmallDatasetBuilder;
import vphshare.driservice.validation.CustomValidationStrategy;
import vphshare.driservice.validation.DatasetValidator;
import vphshare.driservice.validation.ValidationStrategy;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class PerformanceTest {
	
	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("invoke: program <output-file>");
			return;
		}
		
		new PerformanceTest(args[0]).runTest();
	}

	private String outputFile = "/home/kstyrc/Dropbox/MSc/results/performance-test.txt";
	
	public PerformanceTest() {}
	
	public PerformanceTest(String outputFile) {
		this.outputFile = outputFile;
	}

	public void runTest() throws IOException {
		
		File file = new File(outputFile);
		
		if (file.exists())
			FileUtils.deleteQuietly(file);
		PrintWriter writer = new PrintWriter(file);
		
		// 1) prepare the objects and parameters for testing
		
		// a) get necessary data sources
		List<DataSource> dss = DataSourcePreparation.getDataSources();

		// b) get necessary dataset builders
		List<DatasetGenericBuilder> datasetBuilders = Arrays.asList(
				//(DatasetGenericBuilder) new ManySmallFilesDatasetBuilder(),
				//(DatasetGenericBuilder) new FewBigFilesDatasetBuilder(),
				(DatasetGenericBuilder) new RealWorldDataDatasetBuilder(),
				(DatasetGenericBuilder) new SmallDatasetBuilder());
		
		// c) get N, K, threshold values
		List<Integer> numberOfChunks = Arrays.asList(20, 50, 100);
		List<Double> percentageOfChunksPerValidation = Arrays.asList(0.05, 0.1, 0.2);
		List<Long> thresholdSizes = Arrays.asList(1024L, 100000L, 500000L);
		
		// d) how many times to repeat the test to get reliable results
		final int iterations = 1;
		
		// changing parameters loops
		for (DataSource ds : dss) {
			for (DatasetGenericBuilder builder : datasetBuilders) {
				
				// 2) create dataset at the data source & register it as managed
				final MetadataRegistryMock registry = new MetadataRegistryMock();
				ManagedDataset dataset = builder.build(registry, ds);
				
				for (final long threshold : thresholdSizes) {
					for (final int n : numberOfChunks) {
						for (double p : percentageOfChunksPerValidation) {
							final int k = (int) (n * p);
							
							// create injector for testing
							Injector injector = Guice.createInjector(new AbstractModule() {
								
								@Override
								protected void configure() {
									bind(MetadataRegistry.class).toInstance(registry);
									bind(ValidationStrategy.class).to(CustomValidationStrategy.class);
									Properties props = PropertiesProvider.getProperties();
									props.setProperty("validation.chunks.per.item", Integer.toString(n));
									props.setProperty("validation.chunks.per.validation", Integer.toString(k));
									props.setProperty("validation.size.threshold", Long.toString(threshold));
									bindProperties(binder(), props);
								}
							});
							
							StringBuilder text = new StringBuilder();
							text.append("Ready to make performance test with the following parameters:\n");
							text.append("Data source: " + ds.getUrl() + "\n");
							text.append("Dataset: " + dataset.getName() + "\n");
							text.append("N = " + n + "\n");
							text.append("K = " + k + "\n");
							text.append("Threshold = " + threshold + "\n");
							
							// 3) compute dataset checksums
							DatasetValidator validator = injector.getInstance(DatasetValidator.class);
							
							long total = 0L;
							for (int i = 0; i < iterations; i++) {
								long setupStart = System.currentTimeMillis();
								validator.computeChecksums(dataset);
								long setupEnd = System.currentTimeMillis();
								total += setupEnd - setupStart;
							}
							text.append("Computing checksums time = " 
														+ total / iterations / 1000 + "s\n");
							
							// 4) validate dataset integrity
							total = 0L;
							DatasetReport report = null;
							for (int i = 0; i < iterations; i++) {
								long setupStart = System.currentTimeMillis();
								report = validator.validate(dataset);
								long setupEnd = System.currentTimeMillis();
								total += setupEnd - setupStart;
							}
							text.append("Validation check time = " 
														+ total / iterations / 1000 + "s\n");

							// 5) check integrity report
							assert report.isValid();
							
							writer.write(text.toString());
							writer.flush();
						}
					}
				}
				
				// 6) cleanup
				builder.cleanup(dataset, registry, ds);
			}
		}
		
		writer.close();
	}
}
