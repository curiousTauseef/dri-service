package vphshare.driservice.domain;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.List;


public class CloudFile {

	private String id;
	private String name;
	private String path;
	private long size;

	private String checksum;
	private String lastValidationDate;
	
	private List<DataSource> dataSources = Lists.newArrayList();

	public CloudFile(String id, String name, String path, long size) {
		this.id = id;
        this.name = name;
        this.path = path;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
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
        return Objects.hashCode(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CloudFile))
			return false;
		CloudFile other = (CloudFile) obj;
		
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id)) {
			return false;
		}
		
		return true;
	}
}