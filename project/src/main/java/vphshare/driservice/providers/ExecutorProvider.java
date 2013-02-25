package vphshare.driservice.providers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.inject.Provider;

public class ExecutorProvider implements Provider<ExecutorService> {

	private static final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	@Override
	public synchronized ExecutorService get() {
		return executor;
	}
}
