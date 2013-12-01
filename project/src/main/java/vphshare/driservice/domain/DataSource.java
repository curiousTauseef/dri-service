package vphshare.driservice.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSource {

	private String container;
	private String name;
	private String resourceUrl;
	private String username;
	private String password;

    private DataSource() {}

    public DataSource(String container, String name, String resourceUrl, String username, String password) {
        this.container = container;
        this.name = name;
        this.resourceUrl = resourceUrl;
        this.username = username;
        this.password = password;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getName() {
        return name;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
