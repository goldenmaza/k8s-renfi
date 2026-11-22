package org.hellstrand.renfi.shared.persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hellstrand.renfi.shared.model.request.CompareImageRequest;
import org.hellstrand.renfi.shared.model.task.TaskMedia;
import org.hellstrand.renfi.shared.model.task.TaskStatus;
import org.hellstrand.renfi.shared.model.task.TaskType;
import org.hellstrand.renfi.shared.util.ActionUtil;
import org.hellstrand.renfi.shared.util.FileUtil;
import org.hellstrand.renfi.shared.util.JacksonUtil;
import org.hellstrand.renfi.shared.util.UUIDUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class InstructorTaskEntityTest {
    private static final String REQUEST_JSONB = FileUtil.loadJsonResource("instructor_task_compare_image.json");

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Nested
    public class EntityOKCases {
        @Test
        public void instructorTaskEntityIsValidWithAllFields() {
            // Arrange
            var generatedCorrelationId = UUIDUtil.generateUUID();
            var taskType = TaskType.COMPARE;
            var taskMedia = TaskMedia.IMAGE;
            var taskStatus = TaskStatus.PENDING;
            var action = ActionUtil.createAction(taskType, taskMedia, taskStatus, generatedCorrelationId);
            var compareImageRequest = JacksonUtil.fromJson(REQUEST_JSONB, CompareImageRequest.class);

            // Act
            var instructorTaskEntity = new InstructorTaskEntity(
                generatedCorrelationId,
                action,
                taskType,
                taskMedia,
                taskStatus,
                compareImageRequest
            );

            // Assert
            assertEquals(generatedCorrelationId, instructorTaskEntity.getId(), "The entity should have the following CorrelationId, but it didn't.");
            assertEquals(action, instructorTaskEntity.getAction(), "The entity should have the following Action described, but it didn't.");
            assertEquals(TaskType.COMPARE, instructorTaskEntity.getTaskType(), "The entity should have the TaskType set to COMPARE, but it didn't.");
            assertEquals(TaskMedia.IMAGE, instructorTaskEntity.getTaskMedia(), "The entity should have the TaskMedia set to IMAGE, but it didn't.");
            assertEquals(TaskStatus.PENDING, instructorTaskEntity.getTaskStatus(), "The entity should have the TaskStatus set to PENDING, but it didn't.");
            assertEquals(compareImageRequest, instructorTaskEntity.getRequestPayload(), "The entity should have the expected RequestPayload, but it didn't.");
        }
    }

    @Nested
    public class EntityExceptionCases {
        @Test
        public void instructorTaskEntityShouldFailSinceAllRequiredFieldsAreNull() {
            // Arrange
            var instructorTaskEntityInvalid = new InstructorTaskEntity(
                null,
                null,
                null,
                null,
                null,
                null
            );

            // Act
            var invalidEntityViolations = validator.validate(instructorTaskEntityInvalid);

            // Assert
            assertThat(invalidEntityViolations).isNotEmpty();
            assertThat(invalidEntityViolations).hasSize(6);
            var violationMessages = invalidEntityViolations.stream().map(ConstraintViolation::getMessage).toList();
            assertThat(violationMessages).containsExactlyInAnyOrder(
        "The field 'CorrelationId' is required.",
                "The field 'Action' is required.",
                "The field 'TaskType' is required.",
                "The field 'TaskMedia' is required.",
                "The field 'TaskStatus' is required.",
                "The field 'RequestPayload' is required."
            );
        }

        @Test
        public void instructorTaskEntityShouldFailSinceRequestPayloadIsInvalid() {
            // Arrange
            var generatedCorrelationId = UUIDUtil.generateUUID();
            var taskType = TaskType.COMPARE;
            var taskMedia = TaskMedia.IMAGE;
            var taskStatus = TaskStatus.PENDING;
            var action = ActionUtil.createAction(taskType, taskMedia, taskStatus, generatedCorrelationId);

            var compareImageRequestInvalidUnderBoundary = new CompareImageRequest(null, null, -0.1);
            var compareImageRequestInvalidAboveBoundary = new CompareImageRequest("", Collections.emptyList(), 1.1);
            var instructorTaskEntityInvalidUnder = new InstructorTaskEntity(
                generatedCorrelationId,
                action,
                taskType,
                taskMedia,
                taskStatus,
                compareImageRequestInvalidUnderBoundary
            );
            var instructorTaskEntityInvalidAbove = new InstructorTaskEntity(
                generatedCorrelationId,
                action,
                taskType,
                taskMedia,
                taskStatus,
                compareImageRequestInvalidAboveBoundary
            );

            // Act
            var invalidUnderViolations = validator.validate(instructorTaskEntityInvalidUnder);
            var invalidAboveViolations = validator.validate(instructorTaskEntityInvalidAbove);

            // Assert
            assertThat(invalidUnderViolations).isNotEmpty();
            assertThat(invalidUnderViolations).hasSize(3);
            var violationMessages = invalidUnderViolations.stream().map(ConstraintViolation::getMessage).toList();
            assertThat(violationMessages).containsExactlyInAnyOrder(
                "The parameter 'Directory' is required.",
                "The parameter 'ListOfExtensions' is required.",
                "The parameter 'Boundary' must be starting from 0.0."
            );
            assertThat(invalidAboveViolations).isNotEmpty();
            assertThat(invalidAboveViolations).hasSize(3);
            violationMessages = invalidAboveViolations.stream().map(ConstraintViolation::getMessage).toList();
            assertThat(violationMessages).containsExactlyInAnyOrder(
                "The parameter 'Directory' is required.",
                "The parameter 'ListOfExtensions' is required.",
                "The parameter 'Boundary' must be ending at 1.0."
            );
        }
    }
}
