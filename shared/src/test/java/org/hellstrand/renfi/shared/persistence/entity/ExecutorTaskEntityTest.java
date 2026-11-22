package org.hellstrand.renfi.shared.persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hellstrand.renfi.shared.model.response.CompareImageResult;
import org.hellstrand.renfi.shared.model.task.ResultStatus;
import org.hellstrand.renfi.shared.util.FileUtil;
import org.hellstrand.renfi.shared.util.JacksonUtil;
import org.hellstrand.renfi.shared.util.UUIDUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class ExecutorTaskEntityTest {
    private static final String RESULT_JSONB = FileUtil.loadJsonResource("executor_task_compare_image.json");

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Nested
    public class EntityOKCases {
        @Test
        public void executorTaskEntityIsValidWithAllFields() {
            // Arrange
            var generatedCorrelationId = UUIDUtil.generateUUID();
            var resultStatus = ResultStatus.PROCESSING;
            var compareImageResult = JacksonUtil.fromJson(RESULT_JSONB, CompareImageResult.class);

            // Act
            var executorTaskEntity = new ExecutorTaskEntity(
                generatedCorrelationId,
                resultStatus,
                compareImageResult
            );

            // Assert
            assertEquals(generatedCorrelationId, executorTaskEntity.getId(), "The entity should have the following CorrelationId, but it didn't.");
            assertEquals(ResultStatus.PROCESSING, executorTaskEntity.getResultStatus(), "The entity should have the ResultStatus set to PROCESSING, but it didn't.");
            assertEquals(compareImageResult, executorTaskEntity.getResultPayload(), "The entity should have the expected ResultPayload, but it didn't.");
        }
    }

    @Nested
    public class EntityExceptionCases {
        @Test
        public void executorTaskEntityShouldFailSinceAllRequiredFieldsAreNull() {
            // Arrange
            var executorTaskEntityInvalid = new ExecutorTaskEntity(
                null,
                null,
                null
            );

            // Act
            var invalidEntityViolations = validator.validate(executorTaskEntityInvalid);

            // Assert
            assertThat(invalidEntityViolations).isNotEmpty();
            assertThat(invalidEntityViolations).hasSize(3);
            var violationMessages = invalidEntityViolations.stream().map(ConstraintViolation::getMessage).toList();
            assertThat(violationMessages).containsExactlyInAnyOrder(
                "The field 'CorrelationId' is required.",
                "The field 'ResultStatus' is required.",
                "The field 'ResultPayload' is required."
            );
        }

        @Test
        public void executorTaskEntityShouldFailSinceResultPayloadIsInvalid() {
            // Arrange
            var generatedCorrelationId = UUIDUtil.generateUUID();
            var resultStatus = ResultStatus.PROCESSING;
            var compareImageResultInvalidNegativeTime = new CompareImageResult(null, null, null, -1, null);
            var compareImageResultInvalidZeroTime = new CompareImageResult("", resultStatus, Collections.emptyList(), 0, "");
            var executorTaskEntityNegative = new ExecutorTaskEntity(
                generatedCorrelationId,
                resultStatus,
                compareImageResultInvalidNegativeTime
            );
            var executorTaskEntityZero = new ExecutorTaskEntity(
                generatedCorrelationId,
                resultStatus,
                compareImageResultInvalidZeroTime
            );

            // Act
            var invalidNegativeTimeViolations = validator.validate(executorTaskEntityNegative);
            var invalidZeroTimeViolations = validator.validate(executorTaskEntityZero);

            // Assert
            assertThat(invalidNegativeTimeViolations).isNotEmpty();
            assertThat(invalidNegativeTimeViolations).hasSize(5);
            var violationMessages = invalidNegativeTimeViolations.stream().map(ConstraintViolation::getMessage).toList();
            assertThat(violationMessages).containsExactlyInAnyOrder(
                "The parameter 'CorrelationId' is required.",
                "The parameter 'ResultStatus' is required.",
                "The parameter 'ListOfMatchingPair' is required.",
                "The parameter 'ProcessingTime' should be a positive number.",
                "The parameter 'FailureMessage' is required."
            );
            assertThat(invalidZeroTimeViolations).isNotEmpty();
            assertThat(invalidZeroTimeViolations).hasSize(3);
            violationMessages = invalidZeroTimeViolations.stream().map(ConstraintViolation::getMessage).toList();
            assertThat(violationMessages).containsExactlyInAnyOrder(
                "The parameter 'CorrelationId' is required.",
                "The parameter 'ProcessingTime' should be a positive number.",
                "The parameter 'FailureMessage' is required."
            );
        }
    }
}
