package org.hellstrand.renfi.instructor.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hellstrand.renfi.shared.exception.ConnectionLostException;
import org.hellstrand.renfi.shared.model.request.CompareImageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RequestDispatcherTest {
    private RequestSender requestSenderMock;
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    public void setUp() {
        requestSenderMock = mock(RequestSender.class);
        requestDispatcher = new RequestDispatcher(requestSenderMock);
    }

    @Nested
    public class DispatcherOKCases {
        @Test
        public void shouldDelegateCompareImageRequestToSender() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);
            var dispatchedCorrelationId = "kafka-dispatch-001";
            when(requestSenderMock.send(any())).thenReturn(dispatchedCorrelationId);

            // Act
            var receivedCorrelationId = requestDispatcher.dispatch(compareImageRequest);

            // Assert
            assertEquals(dispatchedCorrelationId, receivedCorrelationId, "The dispatcher must return the exact correlation ID provided by the sender.");
            verify(requestSenderMock).send(compareImageRequest);
        }
    }

    @Nested
    public class DispatcherExceptionCases {
        @Test
        public void shouldThrowLostConnectionExceptionWhenConnectionIsLost() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);
            when(requestSenderMock.send(any())).thenThrow(new RuntimeException("Kafka - lost connection"));

            // Act & Assert
            assertThrows(ConnectionLostException.class, () -> requestDispatcher.dispatch(compareImageRequest));
        }
    }
}
