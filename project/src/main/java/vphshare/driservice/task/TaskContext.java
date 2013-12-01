package vphshare.driservice.task;

import com.google.inject.Inject;
import vphshare.driservice.notification.NotificationService;
import vphshare.driservice.registry.MetadataRegistry;
import vphshare.driservice.validation.DatasetValidator;

public class TaskContext {

    private final MetadataRegistry registry;
    private final DatasetValidator validator;
    private final NotificationService notificationService;

    @Inject
    public TaskContext(MetadataRegistry registry, DatasetValidator validator, NotificationService notificationService) {
        this.registry = registry;
        this.validator = validator;
        this.notificationService = notificationService;
    }

    public MetadataRegistry getRegistry() {
        return registry;
    }

    public DatasetValidator getValidator() {
        return validator;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }
}
