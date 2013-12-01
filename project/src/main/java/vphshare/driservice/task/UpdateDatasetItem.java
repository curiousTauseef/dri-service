package vphshare.driservice.task;

import org.apache.log4j.Logger;
import org.joda.time.Duration;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.domain.CloudFile;
import vphshare.driservice.notification.domain.ValidationReport;

public class UpdateDatasetItem extends BaseTask {
    private static final Logger logger = Logger.getLogger(UpdateDatasetItem.class);

    private final CloudDirectory directory;
    private final CloudFile file;

    protected UpdateDatasetItem(CloudDirectory directory, CloudFile file, TaskContext context) {
        super(context);
        this.directory = directory;
        this.file = file;
    }

    @Override
    public void execute(TaskContext context) {
        logger.info(String.format("Updating dataset item checksum [dataset=%s, item=%s]",
                directory.getName(), file.getName()));
        try {
            context.getValidator().computeChecksum(directory, file);
        } catch (Exception e) {
            logger.error(String.format("Updating dataset item checksum failed [dataset=%s, item=%s",
                    directory.getName(), file.getName()), e);
        }
    }

    @Override
    protected String getInfo() {
        return String.format("UpdateFileChecksum{name=%s}", file.getName());
    }
}
