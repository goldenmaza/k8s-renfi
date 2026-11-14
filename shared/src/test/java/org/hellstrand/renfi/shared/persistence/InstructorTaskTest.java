package org.hellstrand.renfi.shared.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.internal.matchers.Null.NULL;

import org.hellstrand.renfi.shared.model.task.TaskMedia;
import org.hellstrand.renfi.shared.model.task.TaskStatus;
import org.hellstrand.renfi.shared.model.task.TaskType;
import org.hellstrand.renfi.shared.persistence.entity.InstructorTaskEntity;
import org.hellstrand.renfi.shared.util.FileUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

public class InstructorTaskTest extends BasePersistenceTest {
    private static final String REQUEST_JSONB = FileUtil.loadJsonResource("instructor_task_compare_image.json");

    @Autowired
    private TaskRepository taskRepository;

    @Nested
    public class TaskOKCases {
        @Test
        public void instructorTaskIsComparePendingImageShouldPersist() {
            // Arrange
            var action = "The task: To " + TaskType.COMPARE + " a(n) " + TaskMedia.IMAGE + " is " + TaskStatus.PENDING;
            var instructorTaskEntity = new InstructorTaskEntity(
                action,
                TaskType.COMPARE,
                TaskMedia.IMAGE,
                TaskStatus.PENDING,
                REQUEST_JSONB
            );

            // Act
            var taskEntity = taskRepository.save(instructorTaskEntity);

            // Assert
            assertEquals(action, taskEntity.getAction(), "The entity should have the following action described.");
            assertEquals(TaskType.COMPARE, taskEntity.getTaskType(), "The entity should have the task type set to COMPARE.");
            assertEquals(TaskMedia.IMAGE, taskEntity.getTaskMedia(), "The entity should have the task media set to IMAGE.");
            assertEquals(TaskStatus.PENDING, taskEntity.getTaskStatus(), "The entity should have the task status set to PENDING.");
            assertEquals(REQUEST_JSONB, taskEntity.getRequestPayload(), "The entity should have the expected request payload.");
            assertNull(taskEntity.getResponsePayload(), "The entity should have null as the response payload.");
        }
    }

    @Nested
    public class ExceptionOKCases {
        @Test
        public void instructorTaskShouldFailSinceActionIsNull() {
            // Arrange
            var instructorTaskEntity = new InstructorTaskEntity(
                null,
                TaskType.COMPARE,
                TaskMedia.IMAGE,
                TaskStatus.PENDING,
                REQUEST_JSONB
            );

            // Act & Assert
            assertThrows(
                DataIntegrityViolationException.class,
                () -> taskRepository.save(instructorTaskEntity),
                "It shouldn't be allowed for the Action to be NULL.");
        }

        @Test
        public void instructorTaskShouldFailSinceTaskTypeIsNull() {
            // Arrange
            var action = "The task: To " + NULL + " a(n) " + TaskMedia.IMAGE + " is " + TaskStatus.PENDING;
            var instructorTaskEntity = new InstructorTaskEntity(
                action,
                null,
                TaskMedia.IMAGE,
                TaskStatus.PENDING,
                REQUEST_JSONB
            );

            // Act & Assert
            assertThrows(
                DataIntegrityViolationException.class,
                () -> taskRepository.save(instructorTaskEntity),
                "It shouldn't be allowed for the TaskType to be NULL.");
        }

        @Test
        public void instructorTaskShouldFailSinceTaskMediaIsNull() {
            // Arrange
            var action = "The task: To " + TaskType.COMPARE + " a(n) " + NULL + " is " + TaskStatus.PENDING;
            var instructorTaskEntity = new InstructorTaskEntity(
                action,
                TaskType.COMPARE,
                null,
                TaskStatus.PENDING,
                REQUEST_JSONB
            );

            // Act & Assert
            assertThrows(
                DataIntegrityViolationException.class,
                () -> taskRepository.save(instructorTaskEntity),
                "It shouldn't be allowed for the TaskMedia to be NULL.");
        }

        @Test
        public void instructorTaskShouldFailSinceTaskStatusIsNull() {
            // Arrange
            var action = "The task: To " + TaskType.COMPARE + " a(n) " + TaskMedia.IMAGE + " is " + NULL;
            var instructorTaskEntity = new InstructorTaskEntity(
                action,
                TaskType.COMPARE,
                TaskMedia.IMAGE,
                null,
                REQUEST_JSONB
            );

            // Act & Assert
            assertThrows(
                DataIntegrityViolationException.class,
                () -> taskRepository.save(instructorTaskEntity),
                "It shouldn't be allowed for the TaskStatus to be NULL.");
        }

        @Test
        public void instructorTaskShouldFailSinceRequestPayloadIsNull() {
            // Arrange
            var action = "The task: To " + TaskType.COMPARE + " a(n) " + TaskMedia.IMAGE + " is " + NULL;
            var instructorTaskEntity = new InstructorTaskEntity(
                action,
                TaskType.COMPARE,
                TaskMedia.IMAGE,
                TaskStatus.PENDING,
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
