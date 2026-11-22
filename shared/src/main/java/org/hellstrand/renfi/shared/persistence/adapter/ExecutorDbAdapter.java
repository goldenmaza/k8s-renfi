package org.hellstrand.renfi.shared.persistence.adapter;

import org.hellstrand.renfi.shared.persistence.ResultRepository;
import org.hellstrand.renfi.shared.persistence.entity.ExecutorTaskEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ExecutorDbAdapter {
    private final ResultRepository resultRepository;

    public ExecutorDbAdapter(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public ExecutorTaskEntity save(ExecutorTaskEntity executorTaskEntity) {
        return resultRepository.save(executorTaskEntity);
    }

    public Optional<ExecutorTaskEntity> findByCorrelationId(UUID correlationId) {
        return resultRepository.findByCorrelationId(correlationId);
    }
}
