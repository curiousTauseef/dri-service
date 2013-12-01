package vphshare.driservice.task;

import org.apache.log4j.Logger;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.registry.MetadataRegistry;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

public class PeriodicScheduler extends TimerTask {
    private static final Logger logger = Logger.getLogger(PeriodicScheduler.class);

    private final MetadataRegistry registry;
    private final TaskBuilder taskBuilder;
    private final ThreadPoolExecutor executor;

    @Inject
    public PeriodicScheduler(
            MetadataRegistry registry,
            TaskBuilder taskBuilder,
            ThreadPoolExecutor executor,
            @Named("periodic.validation.start.delay") long startDelay,
            @Named("periodic.validation.period") long period) {
        this.registry = registry;
        this.taskBuilder = taskBuilder;
        this.executor = executor;
        new Timer("PeriodicScheduler", true).scheduleAtFixedRate(this, startDelay, period);
    }

    @Override
    public void run() {
        try {
            logger.info("Scheduling periodic all-managed-dataset validation tasks");
            List<CloudDirectory> dirs = registry.getCloudDirectories(true);
            for (CloudDirectory dir : dirs) {
                logger.info(String.format(">>> Dataset %s", dir));
                executor.execute(taskBuilder.validateDataset(dir));
            }
            logger.info(String.format("Scheduled [%d] tasks", dirs.size()));
        } catch (RejectedExecutionException ree) {
            logger.error("Task submit reject, maybe execution queue too small?");
        } catch (Exception e) {
            logger.error("Unexpected exception at invoking periodic validation jobs!", e);
        }
    }
}
