package org.hellstrand.renfi.shared.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseTaskEntity {
    @Id
    @NotNull(message = "The field 'CorrelationId' is required.")
    @Column(unique = true, nullable = false)
    private UUID id;

    // Remove once we introduce Lombok
    public void setId(UUID id) {
        this.id = id;
    }
    public UUID getId() {
        return id;
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
