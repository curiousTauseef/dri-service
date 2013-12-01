package vphshare.driservice.registry.lobcder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class LogicalData {

    @XmlElement private String checksum;
    @XmlElement private long lastValidationDate;
    @XmlElement private long length;
    @XmlElement private long modifiedDate;
    @XmlElement private String name;
    @XmlElement private String owner;
    @XmlElement private long parentRef;
    @XmlElement private long pdriGroupId;
    @XmlElement private boolean supervised;
    @XmlElement private DataType type;
    @XmlElement private long uid;

    public String getChecksum() {
        return checksum;
    }

    public long getLastValidationDate() {
        return lastValidationDate;
    }

    public long getLength() {
        return length;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public long getParentRef() {
        return parentRef;
    }

    public long getPdriGroupId() {
        return pdriGroupId;
    }

    public boolean isSupervised() {
        return supervised;
    }

    public DataType getType() {
        return type;
    }

    public long getUid() {
        return uid;
    }

    public boolean isFile() {
        return type == DataType.FILE;
    }

    public boolean isDirectory() {
        return type == DataType.DIRECTORY;
    }
}
