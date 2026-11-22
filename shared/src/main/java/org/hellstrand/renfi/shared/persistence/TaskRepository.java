package org.hellstrand.renfi.shared.persistence;

import org.hellstrand.renfi.shared.persistence.entity.InstructorTaskEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends BaseRepository<InstructorTaskEntity> {
    @Query("""
        SELECT e FROM InstructorTaskEntity e
        WHERE e.id = ?1
    """)
    Optional<InstructorTaskEntity> findByCorrelationId(UUID correlationId);
}
