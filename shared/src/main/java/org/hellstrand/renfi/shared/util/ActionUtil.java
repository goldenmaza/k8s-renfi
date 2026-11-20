package org.hellstrand.renfi.shared.util;

import org.hellstrand.renfi.shared.model.task.TaskMedia;
import org.hellstrand.renfi.shared.model.task.TaskStatus;
import org.hellstrand.renfi.shared.model.task.TaskType;

import java.util.UUID;

public final class ActionUtil {
    public static String createAction(
            final TaskType taskType,
            final TaskMedia taskMedia,
            final TaskStatus taskStatus,
            final UUID correlationId) {
        return "The task: To " + taskType + " a(n) " + taskMedia + " is " + taskStatus + " with ID: " + correlationId;
    }
}
