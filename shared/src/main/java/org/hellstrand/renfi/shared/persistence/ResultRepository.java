package org.hellstrand.renfi.shared.persistence;

import org.hellstrand.renfi.shared.persistence.entity.ExecutorTaskEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResultRepository extends BaseRepository<ExecutorTaskEntity> {
    @Query("""
        SELECT e FROM ExecutorTaskEntity e
        WHERE e.id = ?1
    """)
    Optional<ExecutorTaskEntity> findByCorrelationId(UUID correlationId);
}
