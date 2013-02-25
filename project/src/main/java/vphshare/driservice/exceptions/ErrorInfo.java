package vphshare.driservice.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ErrorInfo {
	
	public static int SEVERITY_ERROR      = 2;
	
	public static int ERROR_TYPE_CLIENT   = 1;
	public static int ERROR_TYPE_INTERNAL = 2;
	public static int ERROR_TYPE_SERVICE  = 3;

    protected Throwable cause                = null;
    protected String    errorId              = null;
    protected String    contextId            = null;

    protected int       errorType            = -1;
    protected int       severity             = -1;

    protected String    userErrorDescription = null;
    protected String    errorDescription     = null;
    protected String    errorCorrection      = null;

    protected Map<String, Object> parameters = new HashMap<String, Object>();

	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

	public String getErrorId() {
		return errorId;
	}

	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public int getErrorType() {
		return errorType;
	}

	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public String getUserErrorDescription() {
		return userErrorDescription;
	}

	public void setUserErrorDescription(String userErrorDescription) {
		this.userErrorDescription = userErrorDescription;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getErrorCorrection() {
		return errorCorrection;
	}

	public void setErrorCorrection(String errorCorrection) {
		this.errorCorrection = errorCorrection;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
}
