package vphshare.driservice.task;

import org.apache.log4j.Logger;
import org.joda.time.Duration;

public abstract class BaseTask implements Executable {
    private static final Logger logger = Logger.getLogger(BaseTask.class);

    private final TaskContext context;

    protected BaseTask(TaskContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        execute(context);
        long end = System.currentTimeMillis();

        logger.info(String.format("Task [%s] finished in [%d seconds]",
                getInfo(), new Duration(start, end).getStandardSeconds()));
    }

    protected abstract String getInfo();
}
