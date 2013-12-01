package vphshare.driservice.domain;

import com.google.common.base.Objects;

public class CloudDirectory implements Comparable<CloudDirectory> {

	private final String id;
	private final String name;
    private final String owner;
    private boolean supervised;

	public CloudDirectory(String id, String name, String owner, boolean supervised) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.supervised = supervised;
	}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isSupervised() {
        return supervised;
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
		if (!(obj instanceof CloudDirectory))
			return false;
		CloudDirectory other = (CloudDirectory) obj;

		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id)) {
			return false;
		}

		return true;
	}

	@Override
	public int compareTo(CloudDirectory o) {
		return this.name.compareTo(o.getName());
	}

    @Override
    public String toString() {
        return Objects.toStringHelper("Directory")
                .add("id", id)
                .add("name", name)
                .toString();
    }
}
