package org.hellstrand.renfi.shared.persistence.adapter;

import org.hellstrand.renfi.shared.persistence.TaskRepository;
import org.hellstrand.renfi.shared.persistence.entity.InstructorTaskEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class InstructorDbAdapter {
    private final TaskRepository taskRepository;

    public InstructorDbAdapter(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public InstructorTaskEntity save(InstructorTaskEntity instructorTaskEntity) {
        return taskRepository.save(instructorTaskEntity);
    }

    public Optional<InstructorTaskEntity> findByCorrelationId(UUID correlationId) {
        return taskRepository.findByCorrelationId(correlationId);
    }
}
