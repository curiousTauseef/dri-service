package vphshare.driservice.aop;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.quartz.SchedulerException;

import vphshare.driservice.exceptions.AppException;
import vphshare.driservice.exceptions.ErrorInfo;
import vphshare.driservice.exceptions.ResourceNotFoundException;
import vphshare.driservice.notification.NotificationService;

public class DRIServiceExceptionHandler implements MethodInterceptor {
	
	@Inject
	private NotificationService notificationService;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try {
			return invocation.proceed();
			
		} catch (SchedulerException se) {
			return Response.status(SERVICE_UNAVAILABLE)
					.entity("DRI was unable to submit requested job to the scheduler").build();
			
		} catch (ResourceNotFoundException nfe) {
			return Response.status(NOT_FOUND).entity(nfe.getMessage()).build();
		
		} catch (AppException ae) {
			ErrorInfo info = ae.getLastErrorInfo();
			notificationService.notifyException(ae);
			return Response.status(SERVICE_UNAVAILABLE).entity(info.getUserErrorDescription()).build();
		}
	}

}
