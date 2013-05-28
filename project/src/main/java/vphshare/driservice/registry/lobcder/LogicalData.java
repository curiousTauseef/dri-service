package vphshare.driservice.registry.lobcder;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LogicalData {
	
	private String checksum;
	private String contentTypes;
	private String contentTypesAsString;
	private long createDate;
	private long lastValidationDate;
	private long length;
	private int lockTimeout;
	private long modifiedDate;
	private String name;
	private String owner;
	private long parentRef;
	private long pdriGroupId;
	private boolean supervised;
	private String type;
	private long uid;

	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	public String getContentTypes() {
		return contentTypes;
	}
	public void setContentTypes(String contentTypes) {
		this.contentTypes = contentTypes;
	}
	public String getContentTypesAsString() {
		return contentTypesAsString;
	}
	public void setContentTypesAsString(String contentTypesAsString) {
		this.contentTypesAsString = contentTypesAsString;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
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
	public int getLockTimeout() {
		return lockTimeout;
	}
	public void setLockTimeout(int lockTimeout) {
		this.lockTimeout = lockTimeout;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
}
