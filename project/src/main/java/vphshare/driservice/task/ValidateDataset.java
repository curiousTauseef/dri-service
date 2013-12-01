package vphshare.driservice.task;

import org.apache.log4j.Logger;
import org.joda.time.Duration;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.notification.Notifications;
import vphshare.driservice.notification.domain.ValidationReport;

public class ValidateDataset  extends BaseTask {
    private static final Logger logger = Logger.getLogger(ValidateDataset.class);

    private final CloudDirectory directory;

    public ValidateDataset(CloudDirectory directory, TaskContext context) {
        super(context);
        this.directory = directory;
    }

    @Override
    public void execute(TaskContext context) {
        logger.info(String.format("Validating dataset %s", directory));
        try {
            ValidationReport report = context.getValidator().validate(directory);

            if (!report.isValid()) {
                context.getNotificationService().sendNotification(Notifications.datasetInvalid(directory));
            }

        } catch (Exception e) {
            logger.error(String.format("Validating dataset failed %s", directory), e);
        }
    }

    @Override
    protected String getInfo() {
        return String.format("ValidateDirectory{name=%s}", directory.getName());
    }
}
