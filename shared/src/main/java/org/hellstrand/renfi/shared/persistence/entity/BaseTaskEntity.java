package org.hellstrand.renfi.shared.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "base_task")
@Entity
public abstract class BaseTaskEntity {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "action", nullable = false)
    private String action;

    // Remove once we introduce Lombok
    public void setAction(String action) {
        this.action = action;
    }
    public String getAction() {
        return action;
    }

    @Column(name = "request_payload", nullable = false, columnDefinition = "jsonb")
    private String requestPayload;

    // Remove once we introduce Lombok
    public void setRequestPayload(String requestPayload) {
        this.requestPayload = requestPayload;
    }
    public String getRequestPayload() {
        return requestPayload;
    }

    @Column(name = "response_payload", columnDefinition = "jsonb")
    private String responsePayload;

    // Remove once we introduce Lombok
    public void setResponsePayload(String responsePayload) {
        this.responsePayload = responsePayload;
    }
    public String getResponsePayload() {
        return responsePayload;
    }

    @Column(name = "created_timestamp", nullable = false)
    private OffsetDateTime created;

    // Remove once we introduce Lombok
    public OffsetDateTime getCreatedTimestamp() {
        return created;
    }

    @Column(name = "updated_timestamp")
    private OffsetDateTime updated;

    // Remove once we introduce Lombok
    public OffsetDateTime getUpdatedTimestamp() {
        return updated;
    }

    @Column(name = "processed_timestamp")
    private OffsetDateTime processed;

    // Remove once we introduce Lombok
    public OffsetDateTime getProcessedTimestamp() {
        return processed;
    }

    @PrePersist
    protected void onCreate() {
        created = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = OffsetDateTime.now();
    }
}
