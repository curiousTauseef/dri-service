package vphshare.driservice.registry.lobcder;

import vphshare.driservice.domain.DataSource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "logicalDataWrapped")
@XmlAccessorType(XmlAccessType.NONE)
public class WrappedLogicalData {

    @XmlElement(required = true)
	private LogicalData logicalData;
    @XmlElement(required = true)
	private String path;
	@XmlElement(name = "pdriList")
	private List<DataSource> dataSources = new ArrayList<DataSource>();
	
	public LogicalData getLogicalData() {
		return logicalData;
	}

	public String getPath() {
		return path;
	}

	public List<DataSource> getDataSources() {
		return dataSources;
	}
}
