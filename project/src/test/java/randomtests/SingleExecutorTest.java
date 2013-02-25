package randomtests;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleExecutorTest {

	public static void main(String[] args) {
		final ExecutorService executor = Executors.newSingleThreadExecutor();
		
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("After sleeping");
				executor.shutdownNow();
			}
		});
		System.out.println("Main thread running");
	}
}
