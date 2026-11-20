package org.hellstrand.renfi.shared.persistence;

import org.hellstrand.renfi.shared.persistence.entity.ExecutorTaskEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends BaseRepository<ExecutorTaskEntity> {
}
