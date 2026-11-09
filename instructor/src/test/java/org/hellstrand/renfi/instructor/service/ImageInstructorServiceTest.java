package org.hellstrand.renfi.instructor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hellstrand.renfi.instructor.request.RequestDispatcher;
import org.hellstrand.renfi.shared.exception.ConnectionLostException;
import org.hellstrand.renfi.shared.model.CompareImageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ImageInstructorServiceTest {
    private RequestDispatcher requestDispatcherMock;
    private ImageInstructorService imageInstructorService;

    @BeforeEach
    public void setUp() {
        requestDispatcherMock = mock(RequestDispatcher.class);
        imageInstructorService = new ImageInstructorService(requestDispatcherMock);
    }

    @Nested
    public class ServiceOKCases {
        @Test
        public void shouldProcessCompareImageRequestSuccessfully() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);
            var dispatchedCorrelationId = "kafka-dispatch-001";
            when(requestDispatcherMock.dispatch(any())).thenReturn(dispatchedCorrelationId);

            // Act
            var receivedCorrelationId = imageInstructorService.processRequest(compareImageRequest);

            // Assert
            assertEquals(dispatchedCorrelationId, receivedCorrelationId, "The dispatcher must return the exact correlation ID provided by the sender.");
            verify(requestDispatcherMock).dispatch(compareImageRequest);
        }
    }

    @Nested
    public class ServiceExceptionCases {
        @Test
        public void shouldPropagateConnectionLostExceptionWhenDispatcherFails() {
            // Arrange
            var compareImageRequest = new CompareImageRequest(null, null, 0.0);
            when(requestDispatcherMock.dispatch(any())).thenThrow(new ConnectionLostException("Kafka - lost connection"));

            // Act & Assert
            assertThrows(ConnectionLostException.class, () -> imageInstructorService.processRequest(compareImageRequest));
        }

        @Test
        public void shouldPropagateUnexpectedRuntimeExceptionWhenDispatcherFails() {
            // Arrange
            var compareImageRequest = new CompareImageRequest(null, null, 0.0);
            when(requestDispatcherMock.dispatch(any())).thenThrow(new RuntimeException("System - Unknown error"));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> imageInstructorService.processRequest(compareImageRequest));
        }
    }
}
