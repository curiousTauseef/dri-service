package vphshare.driservice.domain;

import java.util.List;

import org.apache.commons.collections.Predicate;


public class LogicalData {

	private String id;
	private String name;
	private String path;
	private long size;

	private String checksum;
	private String lastValidationDate;
	
	private List<DataSource> dataSources;

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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

	public String getLastValidationDate() {
		return lastValidationDate;
	}

	public void setLastValidationDate(String lastValidationDate) {
		this.lastValidationDate = lastValidationDate;
	}

	public List<DataSource> getDataSources() {
		return dataSources;
	}

	public void setDataSources(List<DataSource> dataSources) {
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
	
	public static Predicate getPredicate(final String idOrName) {
		return new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				LogicalData item = (LogicalData) object;
				return item.getId().equals(idOrName) || item.getName().equals(idOrName);

			}
		};
	}
}