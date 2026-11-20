package org.hellstrand.renfi.shared.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import org.hellstrand.renfi.shared.model.task.TaskMedia;
import org.hellstrand.renfi.shared.model.task.TaskStatus;
import org.hellstrand.renfi.shared.model.task.TaskType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Table(name = "instructor_task")
@Entity
public class InstructorTaskEntity extends BaseTaskEntity {
    @Column(name = "action", nullable = false)
    private String action;

    // Remove once we introduce Lombok
    public void setAction(String action) {
        this.action = action;
    }
    public String getAction() {
        return action;
    }

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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "request_payload", nullable = false, columnDefinition = "jsonb")
    private String requestPayload;

    // Remove once we introduce Lombok
    public void setRequestPayload(String requestPayload) {
        this.requestPayload = requestPayload;
    }
    public String getRequestPayload() {
        return requestPayload;
    }

    // Remove once we introduce Lombok
    public InstructorTaskEntity() {}
    public InstructorTaskEntity(UUID correlationId, String action, TaskType taskType, TaskMedia taskMedia, TaskStatus taskStatus, String requestPayload) {
        this.setId(correlationId);
        this.setAction(action);
        this.setTaskType(taskType);
        this.setTaskMedia(taskMedia);
        this.setTaskStatus(taskStatus);
        this.setRequestPayload(requestPayload);
    }
}
