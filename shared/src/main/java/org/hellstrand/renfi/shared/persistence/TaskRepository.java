package org.hellstrand.renfi.shared.persistence;

import org.hellstrand.renfi.shared.persistence.entity.InstructorTaskEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends BaseRepository<InstructorTaskEntity> {
}
