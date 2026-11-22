package org.hellstrand.renfi.shared.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hellstrand.renfi.shared.model.request.InstructionRequest;
import org.hellstrand.renfi.shared.model.task.TaskMedia;
import org.hellstrand.renfi.shared.model.task.TaskStatus;
import org.hellstrand.renfi.shared.model.task.TaskType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Table(name = "instructor_task")
@Entity
public class InstructorTaskEntity extends BaseTaskEntity {
    @Size(max = 255)
    @NotBlank(message = "The field 'Action' is required.")
    @Column(name = "action", nullable = false)
    private String action;

    // Remove once we introduce Lombok
    public void setAction(String action) {
        this.action = action;
    }
    public String getAction() {
        return action;
    }

    @NotNull(message = "The field 'TaskType' is required.")
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

    @NotNull(message = "The field 'TaskMedia' is required.")
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

    @NotNull(message = "The field 'TaskStatus' is required.")
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

    @Valid
    @NotNull(message = "The field 'RequestPayload' is required.")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "request_payload", nullable = false, columnDefinition = "jsonb")
    private InstructionRequest requestPayload;

    // Remove once we introduce Lombok
    public void setRequestPayload(InstructionRequest requestPayload) {
        this.requestPayload = requestPayload;
    }
    public InstructionRequest getRequestPayload() {
        return requestPayload;
    }

    // Remove once we introduce Lombok
    public InstructorTaskEntity() {}
    public InstructorTaskEntity(UUID correlationId, String action, TaskType taskType, TaskMedia taskMedia, TaskStatus taskStatus, InstructionRequest requestPayload) {
        this.setId(correlationId);
        this.setAction(action);
        this.setTaskType(taskType);
        this.setTaskMedia(taskMedia);
        this.setTaskStatus(taskStatus);
        this.setRequestPayload(requestPayload);
    }
}
