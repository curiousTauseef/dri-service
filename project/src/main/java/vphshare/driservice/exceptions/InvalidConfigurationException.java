package vphshare.driservice.exceptions;

public class InvalidConfigurationException extends RuntimeException {

	private static final long serialVersionUID = -6816069000555270358L;
	
	public InvalidConfigurationException() {
		super();
	}

	public InvalidConfigurationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidConfigurationException(String arg0) {
		super(arg0);
	}

	public InvalidConfigurationException(Throwable arg0) {
		super(arg0);
	}
}
