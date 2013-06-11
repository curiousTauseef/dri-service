package vphshare.driservice.validation;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.math.random.MersenneTwister;
import org.jclouds.blobstore.AsyncBlobStore;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;

import vphshare.driservice.config.Configuration;
import vphshare.driservice.domain.DataSource;
import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.exceptions.InvalidConfigurationException;
import vphshare.driservice.exceptions.ResourceNotFoundException;

import com.google.common.io.ByteStreams;
import com.google.common.io.InputSupplier;
import com.google.common.util.concurrent.ListenableFuture;

public class CustomValidationStrategy implements ValidationStrategy {
	
	@Inject
	private Configuration cfg;
	
	@Inject
	private DefaultValidationStrategy defaultStrategy;
	
	@Override
	public String setup(CloudDirectory dataset, CloudFile item, DataSource ds, BlobStoreContext context) {
		
		// if the size is 0, then skip
		if (item.getSize() <= 0L)
			return "";
		
		if (item.getSize() < cfg.getValidationSizeThreshold())
			return defaultStrategy.setup(dataset, item, ds, context);
		
		BlobStore blobstore = context.getBlobStore();
		Blob blob = blobstore.getBlob(dataset.getName(), ds.getName());
		if (blob == null)
			throw new ResourceNotFoundException("No such blob or problem with GET options");
		
		long chunkSize = item.getSize() / cfg.getNumberOfChunksPerItem();
		
		final InputStream in = blob.getPayload().getInput();
		StringBuilder checksum = new StringBuilder();
		
		try {
			for (int k = 0; k < cfg.getNumberOfChunksPerItem(); k++) {
					 
				InputSupplier<InputStream> input = ByteStreams.slice(new InputSupplier<InputStream>() {
					@Override
					public InputStream getInput() throws IOException {
						return in;
					}
				}, 0, chunkSize);
				
				String part = DigestUtils.sha256Hex(input.getInput());
				checksum.append(part + ";");
			}
		} catch (IOException e) {
			throw new ResourceNotFoundException("Exception while reading Blob's InputStream");
		} finally {
			IOUtils.closeQuietly(in);
		}
		
		return checksum.toString();
	}
	
	@Override
	public boolean validate(CloudDirectory dataset, CloudFile item, DataSource ds, BlobStoreContext context) {

		// if the size is 0, then skip
		if (item.getSize() <= 0L)
			return true;
		
		if (item.getSize() < cfg.getValidationSizeThreshold())
			return defaultStrategy.validate(dataset, item, ds, context);
		
		AsyncBlobStore blobstore = context.getAsyncBlobStore();
		Map<Integer, ListenableFuture<Blob>> futures = new HashMap<Integer, ListenableFuture<Blob>>();
		MersenneTwister PRNG = new MersenneTwister();
		
		String[] originals = new StrTokenizer(item.getChecksum(), ';').getTokenArray();
		if (originals.length != cfg.getNumberOfChunksPerItem()) {
			throw new InvalidConfigurationException("The number of chunks in AIR register is inconsistent with the " +
					"configuration or AIR content is not consistent with storage data! Please update dataset checksums or update AIR metadata, respectively.");
		}
		
		for (int i = 0; i < cfg.getNumberOfChunksPerValidation(); i++) {
			
			int k = PRNG.nextInt(cfg.getNumberOfChunksPerItem());
			ListenableFuture<Blob> future = BlobstoreHelper.getKthChunk(dataset, item, ds, blobstore, k, cfg.getNumberOfChunksPerItem());

			futures.put(k, future);
		}
		
		for (Map.Entry<Integer, ListenableFuture<Blob>> entry : futures.entrySet()) {
			Blob blob = null;
			try {
				blob = entry.getValue().get();
			} catch (InterruptedException e1) {
				return false;
			} catch (ExecutionException e1) {
				return false;
			} catch (RuntimeException e) {
				return false;
			}
			
			if (blob == null)
				throw new ResourceNotFoundException("No such blob or problem with GET options");
			
			InputStream in = BlobstoreHelper.getInputStream(blob);
			try {
				String checksum = DigestUtils.sha256Hex(in);
				
				if (!checksum.equals(originals[entry.getKey()])) {
					return false;
				}
			
			} catch (IOException e) {
				return false;
			} finally {
				IOUtils.closeQuietly(in);
			}
		}
		
		return true;
	}


}
