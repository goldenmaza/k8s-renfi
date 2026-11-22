package org.hellstrand.renfi.shared.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.validation.ConstraintViolationException;
import org.hellstrand.renfi.configuration.BaseJpaSliceTest;
import org.hellstrand.renfi.shared.model.request.CompareImageRequest;
import org.hellstrand.renfi.shared.model.task.TaskMedia;
import org.hellstrand.renfi.shared.model.task.TaskStatus;
import org.hellstrand.renfi.shared.model.task.TaskType;
import org.hellstrand.renfi.shared.persistence.entity.InstructorTaskEntity;
import org.hellstrand.renfi.shared.util.ActionUtil;
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

@Import({InstructorDbAdapter.class, ValidationAutoConfiguration.class})
public class InstructorDbAdapterTest extends BaseJpaSliceTest {
    private static final String REQUEST_JSONB = FileUtil.loadJsonResource("instructor_task_compare_image.json");

    @Autowired
    private InstructorDbAdapter instructorDbAdapter;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Nested
    public class AdapterOKCases {
        @Test
        public void shouldSaveAndFindInstructorTaskEntitySuccessfully() {
            // Arrange
            var generatedCorrelationId = UUIDUtil.generateUUID();
            var taskType = TaskType.COMPARE;
            var taskMedia = TaskMedia.IMAGE;
            var taskStatus = TaskStatus.PENDING;
            var action = ActionUtil.createAction(taskType, taskMedia, taskStatus, generatedCorrelationId);
            var compareImageRequest = JacksonUtil.fromJson(REQUEST_JSONB, CompareImageRequest.class);
            var instructorTaskEntity = new InstructorTaskEntity(
                generatedCorrelationId,
                action,
                taskType,
                taskMedia,
                taskStatus,
                compareImageRequest
            );

            // Act
            var saved = instructorDbAdapter.save(instructorTaskEntity);
            testEntityManager.flush();
            testEntityManager.clear();

            // Assert
            var fetched = instructorDbAdapter.findByCorrelationId(generatedCorrelationId);
            assertThat(fetched).isPresent();
            assertThat(fetched.get().getId()).isEqualTo(saved.getId());
        }

        @Test
        public void shouldReturnEmptyWhenInstructorTaskEntityDoesNotExist() {
            // Arrange
            var generatedCorrelationId = UUIDUtil.generateUUID();

            // Assert
            var fetched = instructorDbAdapter.findByCorrelationId(generatedCorrelationId);
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
            var taskType = TaskType.COMPARE;
            var taskMedia = TaskMedia.IMAGE;
            var taskStatus = TaskStatus.PENDING;
            var action = ActionUtil.createAction(taskType, taskMedia, taskStatus, generatedCorrelationId);
            var compareImageRequest = JacksonUtil.fromJson(REQUEST_JSONB, CompareImageRequest.class);
            var instructorTaskEntityNormal = new InstructorTaskEntity(
                generatedCorrelationId,
                action,
                taskType,
                taskMedia,
                taskStatus,
                compareImageRequest
            );
            var instructorTaskEntityFailure = new InstructorTaskEntity(
                UUIDUtil.generateUUID(),
                null,
                null,
                null,
                null,
                null
            );

            // Act & Assert
            assertThatThrownBy(
                () -> transactionTemplate.executeWithoutResult(_ -> {
                    instructorDbAdapter.save(instructorTaskEntityNormal);
                    instructorDbAdapter.save(instructorTaskEntityFailure);
                })
            )
            .isInstanceOf(TransactionSystemException.class)
            .hasRootCauseInstanceOf(ConstraintViolationException.class);
            assertThat(instructorDbAdapter.findByCorrelationId(generatedCorrelationId)).isNotPresent();
        }
    }
}
