package vphshare.driservice.task;

import com.google.inject.Inject;
import vphshare.driservice.domain.CloudDirectory;
import vphshare.driservice.domain.CloudFile;

public class TaskBuilder {

    private final TaskContext context;

    @Inject
    public TaskBuilder(TaskContext context) {
        this.context = context;
    }

    public Executable computeChecksums(CloudDirectory directory) {
        return new ComputeChecksums(directory, context);
    }

    public Executable validateDataset(CloudDirectory directory) {
        return new ValidateDataset(directory, context);
    }

    public Executable updateDatasetItem(CloudDirectory directory, CloudFile file) {
        return new UpdateDatasetItem(directory, file, context);
    }
}
