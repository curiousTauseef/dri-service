package vphshare.driservice.exceptions;

import java.util.ArrayList;
import java.util.List;

public class AppException extends Exception {

	private static final long serialVersionUID = -4957231534454386211L;
	
	protected List<ErrorInfo> errorInfoList = new ArrayList<ErrorInfo>();

	public AppException() {
	}

	public ErrorInfo addInfo(ErrorInfo info) {
		errorInfoList.add(info);
		return info;
	}

	public ErrorInfo addInfo() {
		ErrorInfo info = new ErrorInfo();
		errorInfoList.add(info);
		return info;
	}

	public List<ErrorInfo> getErrorInfoList() {
		return errorInfoList;
	}
	
	public ErrorInfo getLastErrorInfo() {
		return errorInfoList.get(errorInfoList.size() - 1);
	}
}
