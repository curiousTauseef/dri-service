package vphshare.driservice.registry.lobcder;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "logicalDataWrappeds")
@XmlAccessorType(XmlAccessType.FIELD)
public class WrappedLogicalDataList {

	@XmlElement(name = "logicalDataWrapped")
    private List<WrappedLogicalData> items;
 
    public WrappedLogicalDataList() {
        items = new ArrayList<WrappedLogicalData>();
    }
 
    public WrappedLogicalDataList(List<WrappedLogicalData> items) {
        this.items = items;
    }
    
    public List<WrappedLogicalData> getItems() {
        return items;
    }
 
}
