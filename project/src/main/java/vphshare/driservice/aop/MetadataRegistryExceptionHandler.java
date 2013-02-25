package vphshare.driservice.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import vphshare.driservice.exceptions.AppException;
import vphshare.driservice.exceptions.ErrorInfo;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

public class MetadataRegistryExceptionHandler implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try {
			return invocation.proceed();
		
		} catch (ClientHandlerException che) {
			throw handleException(che);
			
		} catch (UniformInterfaceException uie) {
			throw handleException(uie);
		}
	}

	private AppException handleException(RuntimeException re) throws AppException {
		AppException exception = new AppException();
		
		ErrorInfo info = exception.addInfo();
		info.setErrorId("AIR registry invocation error");
		info.setContextId("AIR invocation");
		info.setCause(re);
		
		info.setErrorDescription("Error while invoking AIR registry service");
		info.setUserErrorDescription("Unable to fullfill the request due to AIR service error");
		info.setErrorCorrection("Check whether AIR interface or URL changed, check the response");
		
		info.setErrorType(ErrorInfo.ERROR_TYPE_SERVICE);
		info.setSeverity(ErrorInfo.SEVERITY_ERROR);
		
		return exception;
	}

}
