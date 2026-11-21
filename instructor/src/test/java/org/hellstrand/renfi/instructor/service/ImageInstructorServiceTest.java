package org.hellstrand.renfi.instructor.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hellstrand.renfi.instructor.request.RequestDispatcher;
import org.hellstrand.renfi.shared.exception.ConnectionLostException;
import org.hellstrand.renfi.shared.model.request.CompareImageRequest;
import org.hellstrand.renfi.shared.persistence.TaskRepository;
import org.hellstrand.renfi.shared.util.UUIDUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ImageInstructorServiceTest {
    @Mock
    private TaskRepository taskRepositoryMock;

    @Mock
    private RequestDispatcher requestDispatcherMock;

    @InjectMocks
    private ImageInstructorService imageInstructorService;

    @Nested
    public class ServiceOKCases {
        @Test
        public void shouldProcessCompareImageRequestSuccessfully() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);
            when(taskRepositoryMock.save(any())).thenReturn(null);
            when(requestDispatcherMock.dispatch(any(), any())).thenReturn(true);

            // Act
            var receivedCorrelationId = imageInstructorService.processRequest(compareImageRequest);

            // Assert
            assertTrue(UUIDUtil.validateUUID(receivedCorrelationId), "The service must return the exact correlation ID provided by the dispatcher.");
            verify(taskRepositoryMock, times(1)).save(any());
            verify(requestDispatcherMock, times(1)).dispatch(any(), any());
        }
    }

    @Nested
    public class ServiceExceptionCases {
        @Test
        public void shouldPropagateConnectionLostExceptionWhenDispatcherFails() {
            // Arrange
            var compareImageRequest = new CompareImageRequest(null, null, 0.0);
            when(requestDispatcherMock.dispatch(any(), any())).thenThrow(new ConnectionLostException("Kafka - lost connection"));

            // Act & Assert
            assertThrows(ConnectionLostException.class, () -> imageInstructorService.processRequest(compareImageRequest));
        }

        @Test
        public void shouldPropagateUnexpectedRuntimeExceptionWhenDispatcherFails() {
            // Arrange
            var compareImageRequest = new CompareImageRequest(null, null, 0.0);
            when(requestDispatcherMock.dispatch(any(), any())).thenThrow(new RuntimeException("System - Unknown error"));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> imageInstructorService.processRequest(compareImageRequest));
        }
    }
}
