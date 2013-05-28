package vphshare.driservice.registry.lobcder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import vphshare.driservice.domain.DataSource;

@XmlRootElement(name = "logicalDataWrapped")
@XmlAccessorType(XmlAccessType.FIELD)
public class WrappedLogicalData {

	private LogicalData logicalData;
	private String path;
	private String permissions;
	@XmlElement(name = "pdriList")
	private DataSource dataSource;
	
	public LogicalData getLogicalData() {
		return logicalData;
	}
	
	public void setLogicalData(LogicalData logicalData) {
		this.logicalData = logicalData;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getPermissions() {
		return permissions;
	}
	
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
