package vphshare.driservice.task;

import org.apache.log4j.Logger;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.notification.Notifications;

public class ComputeChecksums extends BaseTask {
    private static final Logger logger = Logger.getLogger(ComputeChecksums.class);

    private final CloudDirectory directory;

    public ComputeChecksums(CloudDirectory directory, TaskContext context) {
        super(context);
        this.directory = directory;
    }

    @Override
    public void execute(TaskContext context) {
        logger.info(String.format("Computing dataset checksums %s", directory));
        try {
            context.getValidator().computeChecksums(directory);

        } catch (Exception e) {
            logger.error(String.format("Computing dataset checksums failed %s", directory), e);
            logger.info(String.format("Unsetting dataset %s as supervised", directory));
            context.getRegistry().unsetSupervised(directory);
            context.getNotificationService().sendNotification(Notifications.superviseDatasetFailed(directory, e));
        }
    }

    @Override
    protected String getInfo() {
        return String.format("ComputeDirectoryChecksums{name=%s}", directory.getName());
    }
}
