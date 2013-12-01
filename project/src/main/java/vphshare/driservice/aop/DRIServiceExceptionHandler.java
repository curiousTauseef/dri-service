package vphshare.driservice.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import vphshare.driservice.exceptions.AppException;
import vphshare.driservice.exceptions.ErrorInfo;
import vphshare.driservice.exceptions.ResourceNotFoundException;
import vphshare.driservice.notification.NotificationService;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.concurrent.RejectedExecutionException;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE;

public class DRIServiceExceptionHandler implements MethodInterceptor {
	
	@Inject
	private NotificationService notificationService;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try {
			return invocation.proceed();
			
		} catch (RejectedExecutionException ree) {
			return Response.status(SERVICE_UNAVAILABLE).entity(ree.getMessage()).build();
			
		} catch (ResourceNotFoundException nfe) {
			return Response.status(NOT_FOUND).entity(nfe.getMessage()).build();
		
		} catch (AppException ae) {
			ErrorInfo info = ae.getLastErrorInfo();
			return Response.status(SERVICE_UNAVAILABLE).entity(info.getUserErrorDescription()).build();
		}
	}

}
