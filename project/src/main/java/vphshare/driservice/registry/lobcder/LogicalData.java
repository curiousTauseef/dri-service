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

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public long getLastValidationDate() {
        return lastValidationDate;
    }

    public void setLastValidationDate(long lastValidationDate) {
        this.lastValidationDate = lastValidationDate;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public long getParentRef() {
        return parentRef;
    }

    public void setParentRef(long parentRef) {
        this.parentRef = parentRef;
    }

    public long getPdriGroupId() {
        return pdriGroupId;
    }

    public void setPdriGroupId(long pdriGroupId) {
        this.pdriGroupId = pdriGroupId;
    }

    public boolean isSupervised() {
        return supervised;
    }

    public void setSupervised(boolean supervised) {
        this.supervised = supervised;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public boolean isFile() {
        return type == DataType.FILE;
    }

    public boolean isDirectory() {
        return type == DataType.DIRECTORY;
    }
}
