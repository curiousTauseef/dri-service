package vphshare.driservice.registry.lobcder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "logicalDataWrappeds")
@XmlAccessorType(XmlAccessType.FIELD)
public class WrappedLogicalDataList {

	@XmlElement(name = "logicalDataWrapped")
    private List<WrappedLogicalData> items = new ArrayList<WrappedLogicalData>();

    public List<WrappedLogicalData> getItems() {
        return items;
    }
 
}
