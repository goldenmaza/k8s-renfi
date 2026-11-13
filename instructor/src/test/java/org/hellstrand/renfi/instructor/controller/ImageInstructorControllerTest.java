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
import org.hellstrand.renfi.shared.model.request.CompareImageRequest;
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
            var statusCode = 202;
            var mockedCorrelationId = "kafka-dispatch-001";
            when(imageInstructorServiceMock.processRequest(any())).thenReturn(mockedCorrelationId);

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(statusCode, controllerResponse.getStatusCode().value(), "Request should return HTTP 202 Accepted.");
            assertEquals(mockedCorrelationId, controllerResponse.getBody().body(), "Response should contain the correlation ID in the body.");
        }
    }

    @Nested
    public class ControllerExceptionCases {
        @Test
        public void shouldReturn400BadRequestForInvalidDirectoryNull() {
            // Arrange
            var compareImageRequest = new CompareImageRequest(null, null, 0.0);
            var statusCode = 400;
            var body = "The Directory property cannot be missing or empty.";

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(statusCode, controllerResponse.getStatusCode().value(), "Request should return HTTP 400 BadRequest.");
            assertEquals(body, controllerResponse.getBody().body(), "Request should return the BadRequest message for invalid Directory.");
            verify(imageInstructorServiceMock, never()).processRequest(any());
        }

        @Test
        public void shouldReturn400BadRequestForInvalidDirectoryEmptyString() {
            // Arrange
            var compareImageRequest = new CompareImageRequest("", null, 0.0);
            var statusCode = 400;
            var body = "The Directory property cannot be missing or empty.";

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(statusCode, controllerResponse.getStatusCode().value(), "Request should return HTTP 400 BadRequest.");
            assertEquals(body, controllerResponse.getBody().body(), "Request should return the BadRequest message for invalid Directory.");
            verify(imageInstructorServiceMock, never()).processRequest(any());
        }

        @Test
        public void shouldReturn400BadRequestForInvalidListOfExtensionsNull() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var compareImageRequest = new CompareImageRequest(pathDuplicate, null, 0.0);
            var statusCode = 400;
            var body = "The ListOfExtensions property cannot be missing or an empty list.";

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(statusCode, controllerResponse.getStatusCode().value(), "Request should return HTTP 400 BadRequest.");
            assertEquals(body, controllerResponse.getBody().body(), "Request should return the BadRequest message for invalid ListOfExtensions.");
            verify(imageInstructorServiceMock, never()).processRequest(any());
        }

        @Test
        public void shouldReturn400BadRequestForInvalidListOfExtensionsEmptyList() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var compareImageRequest = new CompareImageRequest(pathDuplicate, Collections.emptyList(), 0.0);
            var statusCode = 400;
            var body = "The ListOfExtensions property cannot be missing or an empty list.";

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(statusCode, controllerResponse.getStatusCode().value(), "Request should return HTTP 400 BadRequest.");
            assertEquals(body, controllerResponse.getBody().body(), "Request should return the BadRequest message for invalid ListOfExtensions.");
            verify(imageInstructorServiceMock, never()).processRequest(any());
        }

        @Test
        public void shouldReturn400BadRequestForBoundaryLessThanZero() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = -0.5;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);
            var statusCode = 400;
            var body = "The Boundary property must be between 0.0 and 1.0 inclusive.";

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(statusCode, controllerResponse.getStatusCode().value(), "Request should return HTTP 400 BadRequest.");
            assertEquals(body, controllerResponse.getBody().body(), "Request should return the BadRequest message for invalid Boundary.");
            verify(imageInstructorServiceMock, never()).processRequest(any());
        }

        @Test
        public void shouldReturn400BadRequestForBoundaryMoreThanOne() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 1.5;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);
            var statusCode = 400;
            var body = "The Boundary property must be between 0.0 and 1.0 inclusive.";

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(statusCode, controllerResponse.getStatusCode().value(), "Request should return HTTP 400 BadRequest.");
            assertEquals(body, controllerResponse.getBody().body(), "Request should return the BadRequest message for invalid Boundary.");
            verify(imageInstructorServiceMock, never()).processRequest(any());
        }

        @Test
        public void shouldReturn500InternalServerErrorForUnexpectedException() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);
            var statusCode = 500;
            var body = "Internal Server Error";
            when(imageInstructorServiceMock.processRequest(any())).thenThrow(new RuntimeException("Kafka - lost connection"));

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(statusCode, controllerResponse.getStatusCode().value(), "Request should return HTTP 500 Internal Server Error.");
            assertEquals(body, controllerResponse.getBody().body(), "Request should return the Internal Server Error message for any exception thrown.");
            verify(imageInstructorServiceMock, times(1)).processRequest(any());
        }

        @Test
        public void shouldReturn503ServiceUnavailableForConnectionLostException() {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);
            var statusCode = 503;
            var body = "Kafka - lost connection";
            when(imageInstructorServiceMock.processRequest(any())).thenThrow(new ConnectionLostException(body));

            // Act
            var controllerResponse = imageInstructorController.handleCompareRequest(compareImageRequest);

            // Assert
            assertEquals(statusCode, controllerResponse.getStatusCode().value(), "Request should return HTTP 503 Service Unavailable.");
            assertEquals(body, controllerResponse.getBody().body(), "Request should return the Service Unavailable message for a connection exception thrown.");
            verify(imageInstructorServiceMock, times(1)).processRequest(any());
        }
    }
}
