package vphshare.driservice.notification.domain;

public enum ValidationStatus {
	VALID,
	UNAVAILABLE,
	INTEGRITY_ERROR;
	
	public boolean isValid() {
		return this.equals(VALID);
	}
	
	public boolean isAvailable() {
		if (this.equals(UNAVAILABLE)) {
			return false;
		} else {
			return true;
		}
	}
	
	public String toString() {
		switch (this) {
		case VALID:
			return "VALID";
		case UNAVAILABLE:
			return "UNAVAILABLE";
		case INTEGRITY_ERROR:
			return "INVALID";
		default:
			return "INVALID";
		}
	}
}
