package org.hellstrand.renfi.instructor.service;

import org.hellstrand.renfi.instructor.model.InstructionApi;
import org.hellstrand.renfi.instructor.request.RequestDispatcher;
import org.hellstrand.renfi.shared.model.request.CompareImageRequest;
import org.hellstrand.renfi.shared.model.request.InstructionRequest;
import org.hellstrand.renfi.shared.model.task.TaskMedia;
import org.hellstrand.renfi.shared.model.task.TaskStatus;
import org.hellstrand.renfi.shared.model.task.TaskType;
import org.hellstrand.renfi.shared.persistence.TaskRepository;
import org.hellstrand.renfi.shared.persistence.entity.InstructorTaskEntity;
import org.hellstrand.renfi.shared.util.ActionUtil;
import org.hellstrand.renfi.shared.util.UUIDUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImageInstructorService {
    private final TaskRepository taskRepository;
    private final RequestDispatcher requestDispatcher;

    public ImageInstructorService(final TaskRepository taskRepository, final RequestDispatcher requestDispatcher) {
        this.taskRepository = taskRepository;
        this.requestDispatcher = requestDispatcher;
    }

    public String processRequest(final InstructionRequest instructionRequest) {
        var correlationId = UUIDUtil.generateUUID();
        var instructorTaskEntity = createTaskEntity(correlationId, instructionRequest);
        taskRepository.save(instructorTaskEntity);
        requestDispatcher.dispatch(correlationId.toString(), instructionRequest);

        return correlationId.toString();
    }

    private InstructorTaskEntity createTaskEntity(final UUID correlationId, final InstructionRequest instructionRequest) {
        return switch (instructionRequest) {
            case CompareImageRequest _ -> {
                var requestPayload = InstructionApi.JSON_MAPPER.writeValueAsString(instructionRequest);
                var taskType = taskType(instructionRequest);
                var taskMedia = taskMedia(instructionRequest);
                var taskStatus = TaskStatus.PENDING;
                var action = ActionUtil.createAction(taskType, taskMedia, taskStatus, correlationId);
                yield new InstructorTaskEntity(correlationId, action, taskType, taskMedia, taskStatus, requestPayload);
            }
        };
    }

    private TaskType taskType(final InstructionRequest instructionRequest) {
        return switch (instructionRequest) {
            case CompareImageRequest _ -> TaskType.COMPARE;
        };
    }

    private TaskMedia taskMedia(final InstructionRequest instructionRequest) {
        return switch (instructionRequest) {
            case CompareImageRequest _ -> TaskMedia.IMAGE;
        };
    }
}
