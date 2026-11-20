package org.hellstrand.renfi.shared.persistence;

import org.hellstrand.renfi.shared.persistence.entity.BaseTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<T extends BaseTaskEntity> extends JpaRepository<T, UUID> {
}
