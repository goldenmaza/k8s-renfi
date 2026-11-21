package org.hellstrand.renfi.shared.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hellstrand.renfi.configuration.BasePersistenceTest;
import org.hellstrand.renfi.shared.model.task.TaskMedia;
import org.hellstrand.renfi.shared.model.task.TaskStatus;
import org.hellstrand.renfi.shared.model.task.TaskType;
import org.hellstrand.renfi.shared.persistence.TaskRepository;
import org.hellstrand.renfi.shared.util.ActionUtil;
import org.hellstrand.renfi.shared.util.FileUtil;
import org.hellstrand.renfi.shared.util.UUIDUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

public class InstructorTaskEntityTest extends BasePersistenceTest {
    private static final String REQUEST_JSONB = FileUtil.loadJsonResource("instructor_task_compare_image.json");

    @Autowired
    private TaskRepository taskRepository;

    @Nested
    public class TaskOKCases {
        @Test
        public void instructorTaskIsComparePendingImageShouldPersist() {
            // Arrange
            var generatedCorrelationId = UUIDUtil.generateUUID();
            var taskType = TaskType.COMPARE;
            var taskMedia = TaskMedia.IMAGE;
            var taskStatus = TaskStatus.PENDING;
            var action = ActionUtil.createAction(taskType, taskMedia, taskStatus, generatedCorrelationId);
            var instructorTaskEntity = new InstructorTaskEntity(
                generatedCorrelationId,
                action,
                taskType,
                taskMedia,
                taskStatus,
                REQUEST_JSONB
            );

            // Act
            var taskEntity = taskRepository.save(instructorTaskEntity);

            // Assert
            assertEquals(generatedCorrelationId, taskEntity.getId(), "The entity should have the following CorrelationId.");
            assertEquals(action, taskEntity.getAction(), "The entity should have the following Action described.");
            assertEquals(TaskType.COMPARE, taskEntity.getTaskType(), "The entity should have the TaskType set to COMPARE.");
            assertEquals(TaskMedia.IMAGE, taskEntity.getTaskMedia(), "The entity should have the TaskMedia set to IMAGE.");
            assertEquals(TaskStatus.PENDING, taskEntity.getTaskStatus(), "The entity should have the TaskStatus set to PENDING.");
            assertEquals(REQUEST_JSONB, taskEntity.getRequestPayload(), "The entity should have the expected RequestPayload.");
        }
    }

    @Nested
    public class TaskExceptionCases {
        @Test
        public void instructorTaskShouldFailSinceCorrelationIdIsNull() {
            // Arrange
            var taskType = TaskType.COMPARE;
            var taskMedia = TaskMedia.IMAGE;
            var taskStatus = TaskStatus.PENDING;
            var action = ActionUtil.createAction(taskType, taskMedia, taskStatus, null);
            var instructorTaskEntity = new InstructorTaskEntity(
                null,
                action,
                taskType,
                taskMedia,
                taskStatus,
                REQUEST_JSONB
            );

            // Act & Assert
            assertThrows(
                JpaSystemException.class,
                () -> taskRepository.save(instructorTaskEntity),
                "It shouldn't be allowed for the CorrelationId to be NULL.");
        }

        @Test
        public void instructorTaskShouldFailSinceActionIsNull() {
            // Arrange
            var generatedCorrelationId = UUIDUtil.generateUUID();
            var taskType = TaskType.COMPARE;
            var taskMedia = TaskMedia.IMAGE;
            var taskStatus = TaskStatus.PENDING;
            var instructorTaskEntity = new InstructorTaskEntity(
                generatedCorrelationId,
                null,
                taskType,
                taskMedia,
                taskStatus,
                REQUEST_JSONB
            );

            // Act & Assert
            assertThrows(
                DataIntegrityViolationException.class,
                () -> taskRepository.save(instructorTaskEntity),
                "It shouldn't be allowed for the Action to be NULL.");
        }

        @Test
        public void instructorTaskShouldFailSinceRequestPayloadIsNull() {
            // Arrange
            var generatedCorrelationId = UUIDUtil.generateUUID();
            var taskType = TaskType.COMPARE;
            var taskMedia = TaskMedia.IMAGE;
            var taskStatus = TaskStatus.PENDING;
            var action = ActionUtil.createAction(taskType, taskMedia, taskStatus, generatedCorrelationId);
            var instructorTaskEntity = new InstructorTaskEntity(
                generatedCorrelationId,
                action,
                taskType,
                taskMedia,
                taskStatus,
                null
            );

            // Act & Assert
            assertThrows(
                DataIntegrityViolationException.class,
                () -> taskRepository.save(instructorTaskEntity),
                "It shouldn't be allowed for the RequestPayload to be NULL.");
        }
    }
}
