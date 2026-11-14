package org.hellstrand.renfi.shared.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import org.hellstrand.renfi.shared.model.task.TaskMedia;
import org.hellstrand.renfi.shared.model.task.TaskStatus;
import org.hellstrand.renfi.shared.model.task.TaskType;

@Table(name = "instructor_task")
@Entity
public class InstructorTaskEntity extends BaseTaskEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private TaskType taskType;

    // Remove once we introduce Lombok
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
    public TaskType getTaskType() {
        return taskType;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "task_media", nullable = false)
    private TaskMedia taskMedia;

    // Remove once we introduce Lombok
    public void setTaskMedia(TaskMedia taskMedia) {
        this.taskMedia = taskMedia;
    }
    public TaskMedia getTaskMedia() {
        return taskMedia;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status", nullable = false)
    private TaskStatus taskStatus;

    // Remove once we introduce Lombok
    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    // Remove once we introduce Lombok
    public InstructorTaskEntity(String action, TaskType taskType, TaskMedia taskMedia, TaskStatus taskStatus, String requestPayload) {
        this.setAction(action);
        this.setTaskType(taskType);
        this.setTaskMedia(taskMedia);
        this.setTaskStatus(taskStatus);
        this.setRequestPayload(requestPayload);
    }
}
