package org.hellstrand.renfi.instructor.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hellstrand.renfi.instructor.service.ImageInstructorService;
import org.hellstrand.renfi.shared.exception.ConnectionLostException;
import org.hellstrand.renfi.shared.model.CompareImageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class ImageInstructorControllerTest {
    private ImageInstructorService imageInstructorServiceMock;
    private ImageInstructorController imageInstructorController;

    @BeforeEach
    public void setUp() {
        imageInstructorServiceMock = mock(ImageInstructorService.class);
        imageInstructorController = new ImageInstructorController(imageInstructorServiceMock);
    }

    @Nested
    public class ControllerOKCases {
        @Test
        public void shouldReturnAcceptedWhenRequestIsValid() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);
            var dispatchedCorrelationId = "kafka-dispatch-001";
            when(imageInstructorServiceMock.processRequest(any())).thenReturn(dispatchedCorrelationId);

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(202, controllerResponse.status(), "Request should return HTTP 202 Accepted.");
            assertEquals(dispatchedCorrelationId, controllerResponse.body(), "Response should contain the correlation ID in the body.");
        }
    }

    @Nested
    public class ControllerExceptionCases {
        @Test
        public void shouldReturn400BadRequestForInvalidDirectoryNull() {
            // Arrange
            var compareImageRequest = new CompareImageRequest(null, null, 0.0);

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(400, controllerResponse.status(), "Request should return HTTP 400 BadRequest.");
            verify(imageInstructorServiceMock, never()).processRequest(any());
        }

        @Test
        public void shouldReturn400BadRequestForInvalidDirectoryEmptyString() {
            // Arrange
            var compareImageRequest = new CompareImageRequest("", null, 0.0);

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(400, controllerResponse.status(), "Request should return HTTP 400 BadRequest.");
            verify(imageInstructorServiceMock, never()).processRequest(any());
        }

        @Test
        public void shouldReturn400BadRequestForInvalidListOfExtensionsNull() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var compareImageRequest = new CompareImageRequest(pathDuplicate, null, 0.0);

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(400, controllerResponse.status(), "Request should return HTTP 400 BadRequest.");
            verify(imageInstructorServiceMock, never()).processRequest(any());
        }

        @Test
        public void shouldReturn400BadRequestForInvalidListOfExtensionsEmptyList() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var compareImageRequest = new CompareImageRequest(pathDuplicate, Collections.emptyList(), 0.0);

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(400, controllerResponse.status(), "Request should return HTTP 400 BadRequest.");
            verify(imageInstructorServiceMock, never()).processRequest(any());
        }

        @Test
        public void shouldReturn400BadRequestForBoundaryLessThanZero() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = -0.5;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(400, controllerResponse.status(), "Request should return HTTP 400 BadRequest.");
            verify(imageInstructorServiceMock, never()).processRequest(any());
        }

        @Test
        public void shouldReturn400BadRequestForBoundaryMoreThanOne() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 1.5;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(400, controllerResponse.status(), "Request should return HTTP 400 BadRequest.");
            verify(imageInstructorServiceMock, never()).processRequest(any());
        }

        @Test
        public void shouldReturn500InternalServerErrorForUnexpectedException() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);
            when(imageInstructorServiceMock.processRequest(any())).thenThrow(new RuntimeException("Kafka - lost connection"));

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(500, controllerResponse.status(), "Request should return HTTP 500 Internal Server Error.");
            verify(imageInstructorServiceMock, times(1)).processRequest(any());
        }

        @Test
        public void shouldReturn503ServiceUnavailableForConnectionLostException() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);
            when(imageInstructorServiceMock.processRequest(any())).thenThrow(new ConnectionLostException("Kafka - lost connection"));

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(503, controllerResponse.status(), "Request should return HTTP 503 Service Unavailable.");
            verify(imageInstructorServiceMock, times(1)).processRequest(any());
        }
    }
}
