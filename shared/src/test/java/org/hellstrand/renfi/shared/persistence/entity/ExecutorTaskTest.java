package org.hellstrand.renfi.shared.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hellstrand.renfi.configuration.BasePersistenceTest;
import org.hellstrand.renfi.shared.model.task.ResultStatus;
import org.hellstrand.renfi.shared.persistence.ResultRepository;
import org.hellstrand.renfi.shared.util.FileUtil;
import org.hellstrand.renfi.shared.util.UUIDUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

public class ExecutorTaskTest extends BasePersistenceTest {
    private static final String RESULT_JSONB = FileUtil.loadJsonResource("instructor_task_compare_image.json");

    @Autowired
    private ResultRepository resultRepository;

    @Nested
    public class ResultOKCases {
        @Test
        public void executorTaskIsComparePendingImageShouldPersist() {
            // Arrange
            var generatedCorrelationId = UUIDUtil.generateUUID();
            var executorTaskEntity = new ExecutorTaskEntity(
                generatedCorrelationId,
                ResultStatus.PROCESSING,
                RESULT_JSONB
            );

            // Act
            var resultEntity = resultRepository.save(executorTaskEntity);

            // Assert
            assertEquals(generatedCorrelationId, resultEntity.getId(), "The entity should have the following CorrelationId.");
            assertEquals(ResultStatus.PROCESSING, resultEntity.getResultStatus(), "The entity should have the ResultStatus set to PROCESSING.");
            assertEquals(RESULT_JSONB, resultEntity.getResultPayload(), "The entity should have the expected ResultPayload.");
        }
    }

    @Nested
    public class ResultExceptionCases {
        @Test
        public void instructorTaskShouldFailSinceCorrelationIdIsNull() {
            // Arrange
            var executorTaskEntity = new ExecutorTaskEntity(
                null,
                ResultStatus.PROCESSING,
                RESULT_JSONB
            );

            // Act & Assert
            assertThrows(
                JpaSystemException.class,
                () -> resultRepository.save(executorTaskEntity),
                "It shouldn't be allowed for the CorrelationId to be NULL.");
        }

        @Test
        public void instructorTaskShouldFailSinceResultPayloadIsNull() {
            // Arrange
            var generatedCorrelationId = UUIDUtil.generateUUID();
            var executorTaskEntity = new ExecutorTaskEntity(
                generatedCorrelationId,
                ResultStatus.PROCESSING,
                null
            );

            // Act & Assert
            assertThrows(
                DataIntegrityViolationException.class,
                () -> resultRepository.save(executorTaskEntity),
                "It shouldn't be allowed for the ResultPayload to be NULL.");
        }
    }
}
