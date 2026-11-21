package org.hellstrand.renfi.instructor.request;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hellstrand.renfi.shared.exception.ConnectionLostException;
import org.hellstrand.renfi.shared.model.request.CompareImageRequest;
import org.hellstrand.renfi.shared.util.UUIDUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RequestDispatcherTest {
    @Mock
    private RequestSender requestSenderMock;

    @InjectMocks
    private RequestDispatcher requestDispatcher;

    @Nested
    public class DispatcherOKCases {
        @Test
        public void shouldDispatchCompareImageRequestSuccessfully() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);
            var generatedCorrelationId = UUIDUtil.generateUUID().toString();
            when(requestSenderMock.send(any(), any())).thenReturn(true);

            // Act
            var dispatched = requestDispatcher.dispatch(generatedCorrelationId, compareImageRequest);

            // Assert
            assertTrue(dispatched, "The dispatcher should return true as the delegation with the sender was successful.");
            verify(requestSenderMock, times(1)).send(generatedCorrelationId, compareImageRequest);
        }
    }

    @Nested
    public class DispatcherExceptionCases {
        @Test
        public void shouldThrowLostConnectionExceptionWhenConnectionIsLost() {
            // Arrange
            var generatedCorrelationId = UUIDUtil.generateUUID().toString();
            var compareImageRequest = new CompareImageRequest(null, null, 0.0);
            when(requestSenderMock.send(any(), any())).thenThrow(new RuntimeException("Kafka - lost connection"));

            // Act & Assert
            assertThrows(ConnectionLostException.class, () -> requestDispatcher.dispatch(generatedCorrelationId, compareImageRequest));
        }
    }
}
