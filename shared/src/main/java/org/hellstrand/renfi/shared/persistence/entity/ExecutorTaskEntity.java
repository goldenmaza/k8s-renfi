package org.hellstrand.renfi.shared.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hellstrand.renfi.shared.model.response.InstructionResult;
import org.hellstrand.renfi.shared.model.task.ResultStatus;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Table(name = "executor_task")
@Entity
public class ExecutorTaskEntity extends BaseTaskEntity {
    @NotNull(message = "The field 'ResultStatus' is required.")
    @Enumerated(EnumType.STRING)
    @Column(name = "result_status", nullable = false)
    private ResultStatus resultStatus;

    // Remove once we introduce Lombok
    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }
    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    @Valid
    @NotNull(message = "The field 'ResultPayload' is required.")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "result_payload", nullable = false, columnDefinition = "jsonb")
    private InstructionResult resultPayload;

    // Remove once we introduce Lombok
    public void setResultPayload(InstructionResult resultPayload) {
        this.resultPayload = resultPayload;
    }
    public InstructionResult getResultPayload() {
        return resultPayload;
    }

    // Remove once we introduce Lombok
    public ExecutorTaskEntity() {}
    public ExecutorTaskEntity(UUID correlationId, ResultStatus resultStatus, InstructionResult resultPayload) {
        this.setId(correlationId);
        this.setResultStatus(resultStatus);
        this.setResultPayload(resultPayload);
    }
}
