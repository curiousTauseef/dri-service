package vphshare.driservice.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.collections.Predicate;
import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LogicalData {

	@JsonProperty("_id")
	private String id;

	@JsonProperty("identifier")
	private String identifier;
	
	@JsonProperty("data_set_name")
	private String datasetName;

	@JsonProperty("size")
	private long size;

	@JsonProperty("checksum")
	private String checksum;

	@JsonProperty("last_accessed")
	private String lastAccessed;

	@JsonProperty("last_modified")
	private String lastModified;

	@JsonProperty("dri_checksum")
	private String driChecksum;

	@JsonProperty("dri_checksum_update_time")
	private String driChecksumUpdateTime;
	
	@JsonProperty("data_source_ids")
	private List<String> dataSources;

	public LogicalData() {
	}

	public LogicalData(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String getLastAccessed() {
		return lastAccessed;
	}

	public void setLastAccessed(String lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getDriChecksum() {
		return driChecksum;
	}

	public void setDriChecksum(String driChecksum) {
		this.driChecksum = driChecksum;
	}

	public String getDriChecksumUpdateTime() {
		return driChecksumUpdateTime;
	}

	public void setDriChecksumUpdateTime(String checksumUpdateTime) {
		this.driChecksumUpdateTime = checksumUpdateTime;
	}

	public List<String> getDataSources() {
		return dataSources;
	}

	public void setDataSources(List<String> dataSources) {
		this.dataSources = dataSources;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LogicalData))
			return false;
		LogicalData other = (LogicalData) obj;
		
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id)) {
			return false;
		}
		
		return true;
	}
	
	public static Predicate getPredicate(final String itemIDorIdentifier) {
		return new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				LogicalData item = (LogicalData) object;
				return item.getId().equals(itemIDorIdentifier) || item.getIdentifier().equals(itemIDorIdentifier);

			}
		};
	}
}