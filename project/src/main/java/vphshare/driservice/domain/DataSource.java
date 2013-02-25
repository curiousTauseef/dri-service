package vphshare.driservice.domain;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSource {
	
	public static final String S3_DATA_SOURCE = "S3DataSource";
	public static final String SWIFT_DATA_SOURCE = "SwiftDataSource"; 

	@JsonProperty("_id")
	private String id;

	@JsonProperty("data_source_type")
	private String type;

	@JsonProperty("name")
	private String name;

	@JsonProperty("url")
	private String url;

	@JsonProperty("description")
	private String description;

	@JsonProperty("account_name")
	private String accountName;

	@JsonProperty("access_key_id")
	private String accessID;

	@JsonProperty("swift_users")
	private List<SwiftUser> swiftUsers;

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class SwiftUser {
		@JsonProperty("_id")
		private String id;
		
		@JsonProperty("name")
		private String name;
		
		@JsonProperty("role")
		private String role;

		public SwiftUser() {
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

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}
	}
	
	public DataSource() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccessID() {
		return accessID;
	}

	public void setAccessID(String accessID) {
		this.accessID = accessID;
	}

	public List<SwiftUser> getSwiftUsers() {
		return Collections.unmodifiableList(swiftUsers);
	}
}
