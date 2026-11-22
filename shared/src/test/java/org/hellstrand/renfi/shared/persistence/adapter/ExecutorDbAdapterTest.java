package org.hellstrand.renfi.shared.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.validation.ConstraintViolationException;
import org.hellstrand.renfi.configuration.BaseJpaSliceTest;
import org.hellstrand.renfi.shared.model.response.CompareImageResult;
import org.hellstrand.renfi.shared.model.task.ResultStatus;
import org.hellstrand.renfi.shared.persistence.entity.ExecutorTaskEntity;
import org.hellstrand.renfi.shared.util.FileUtil;
import org.hellstrand.renfi.shared.util.JacksonUtil;
import org.hellstrand.renfi.shared.util.UUIDUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.validation.autoconfigure.ValidationAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Import({ExecutorDbAdapter.class, ValidationAutoConfiguration.class})
public class ExecutorDbAdapterTest extends BaseJpaSliceTest {
    private static final String RESULT_JSONB = FileUtil.loadJsonResource("executor_task_compare_image.json");

    @Autowired
    private ExecutorDbAdapter executorDbAdapter;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Nested
    public class AdapterOKCases {
        @Test
        public void shouldSaveAndFindExecutorTaskEntitySuccessfully() {
            // Arrange
            var generatedCorrelationId = UUIDUtil.generateUUID();
            var resultStatus = ResultStatus.PROCESSING;
            var compareImageResult = JacksonUtil.fromJson(RESULT_JSONB, CompareImageResult.class);
            var executorTaskEntity = new ExecutorTaskEntity(
                generatedCorrelationId,
                resultStatus,
                compareImageResult
            );

            // Act
            var saved = executorDbAdapter.save(executorTaskEntity);
            testEntityManager.flush();
            testEntityManager.clear();

            // Assert
            var fetched = executorDbAdapter.findByCorrelationId(generatedCorrelationId);
            assertThat(fetched).isPresent();
            assertThat(fetched.get().getId()).isEqualTo(saved.getId());
        }

        @Test
        public void shouldReturnEmptyWhenExecutorTaskEntityDoesNotExist() {
            // Arrange
            var generatedCorrelationId = UUIDUtil.generateUUID();

            // Assert
            var fetched = executorDbAdapter.findByCorrelationId(generatedCorrelationId);
            assertThat(fetched).isNotPresent();
        }
    }

    @Nested
    public class AdapterExceptionCases {
        @Test
        @Transactional(propagation = Propagation.NOT_SUPPORTED)
        public void shouldRollbackWhenSecondSaveFails() {
            // Arrange
            var generatedCorrelationId = UUIDUtil.generateUUID();
            var resultStatus = ResultStatus.PROCESSING;
            var compareImageResult = JacksonUtil.fromJson(RESULT_JSONB, CompareImageResult.class);
            var executorTaskEntityNormal = new ExecutorTaskEntity(
                generatedCorrelationId,
                resultStatus,
                compareImageResult
            );
            var executorTaskEntityFailure = new ExecutorTaskEntity(
                UUIDUtil.generateUUID(),
                null,
                null
            );

            // Act & Assert
            assertThatThrownBy(
                () -> transactionTemplate.executeWithoutResult(_ -> {
                    executorDbAdapter.save(executorTaskEntityNormal);
                    executorDbAdapter.save(executorTaskEntityFailure);
                })
            )
            .isInstanceOf(TransactionSystemException.class)
            .hasRootCauseInstanceOf(ConstraintViolationException.class);
            assertThat(executorDbAdapter.findByCorrelationId(generatedCorrelationId)).isNotPresent();
        }
    }
}
