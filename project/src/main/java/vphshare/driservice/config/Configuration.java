package vphshare.driservice.config;

import javax.inject.Inject;
import javax.inject.Named;

public class Configuration {
	
	@Inject @Named("validation.chunks.per.item")
	private int numberOfChunksPerItem;
	
	@Inject @Named("validation.chunks.per.validation")
	private int numberOfChunksPerValidation;
	
	@Inject @Named("validation.size.threshold")
	private long validationSizeThreshold;

	
	@Inject @Named("validation.period")
	private int validationPeriod;
	
	@Inject @Named("validation.start.delay")
	private long validationStartDelay;
	
	public int getNumberOfChunksPerItem() {
		return numberOfChunksPerItem;
	}
	
	public int getNumberOfChunksPerValidation() {
		return numberOfChunksPerValidation;
	}
	
	public long getValidationSizeThreshold() {
		return validationSizeThreshold;
	}
	
	public int getValidationPeriod() {
		return validationPeriod;
	}
	
	public long getValidationStartDelay() {
		return validationStartDelay;
	}
}
