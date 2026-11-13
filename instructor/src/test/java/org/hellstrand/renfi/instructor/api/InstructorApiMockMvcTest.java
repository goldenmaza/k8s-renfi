package org.hellstrand.renfi.instructor.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hellstrand.renfi.instructor.controller.ImageInstructorController;
import org.hellstrand.renfi.instructor.model.ApiResponse;
import org.hellstrand.renfi.instructor.model.InstructionApi;
import org.hellstrand.renfi.instructor.service.ImageInstructorService;
import org.hellstrand.renfi.shared.exception.ConnectionLostException;
import org.hellstrand.renfi.shared.model.CompareImageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

public class InstructorApiMockMvcTest {
    private ImageInstructorService imageInstructorServiceMock;
    private ImageInstructorController imageInstructorController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        imageInstructorServiceMock = mock(ImageInstructorService.class);
        imageInstructorController = new ImageInstructorController(imageInstructorServiceMock);

        mockMvc = MockMvcBuilders.standaloneSetup(imageInstructorController).build();
    }

    @Nested
    public class MockOKCases {
        @Test
        public void shouldCallInstructionCompareEndpointAndExpect200Response() throws Exception {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);
            var mockedCorrelationId = "kafka-dispatch-001";
            when(imageInstructorServiceMock.processRequest(compareImageRequest)).thenReturn(mockedCorrelationId);

            // Act
            var mockCall = mockMvc.perform(
                    post(InstructionApi.V1_INSTRUCTION_COMPARE)
                        .headers(InstructionApi.HEADERS_JSON)
                        .content(InstructionApi.JSON_MAPPER.writeValueAsString(compareImageRequest)))
                .andExpect(status().isAccepted())
                .andReturn();

            // Assert
            var content = mockCall.getResponse().getContentAsString();
            var apiResponse = InstructionApi.JSON_MAPPER.readValue(content, ApiResponse.class);
            assertEquals(mockedCorrelationId, apiResponse.body(), "Response should contain the mocked correlation ID.");
        }
    }

    @Nested
    public class MockExceptionCases {
        @Test
        public void shouldReturn400BadRequestForInvalidDirectoryNull() throws Exception {
            // Arrange
            var compareImageRequest = new CompareImageRequest(null, null, 0.0);
            var statusCode = 400;
            var body = "The Directory property cannot be missing or empty.";

            // Act
            var mockCall = mockMvc.perform(
                    post(InstructionApi.V1_INSTRUCTION_COMPARE)
                        .headers(InstructionApi.HEADERS_JSON)
                        .content(InstructionApi.JSON_MAPPER.writeValueAsString(compareImageRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

            // Assert
            var content = mockCall.getResponse().getContentAsString();
            var apiResponse = InstructionApi.JSON_MAPPER.readValue(content, ApiResponse.class);
            assertEquals(statusCode, apiResponse.status(), "Request should return HTTP 400 BadRequest.");
            assertEquals(body, apiResponse.body(), "Request should return the BadRequest message for invalid Directory.");
        }

        @Test
        public void shouldReturn400BadRequestForInvalidDirectoryEmptyString() throws Exception {
            // Arrange
            var compareImageRequest = new CompareImageRequest("", null, 0.0);
            var statusCode = 400;
            var body = "The Directory property cannot be missing or empty.";

            // Act
            var mockCall = mockMvc.perform(
                    post(InstructionApi.V1_INSTRUCTION_COMPARE)
                        .headers(InstructionApi.HEADERS_JSON)
                        .content(InstructionApi.JSON_MAPPER.writeValueAsString(compareImageRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

            // Assert
            var content = mockCall.getResponse().getContentAsString();
            var apiResponse = InstructionApi.JSON_MAPPER.readValue(content, ApiResponse.class);
            assertEquals(statusCode, apiResponse.status(), "Request should return HTTP 400 BadRequest.");
            assertEquals(body, apiResponse.body(), "Request should return the BadRequest message for invalid Directory.");
        }

        @Test
        public void shouldReturn400BadRequestForInvalidListOfExtensionsNull() throws Exception {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var compareImageRequest = new CompareImageRequest(pathDuplicate, null, 0.0);
            var statusCode = 400;
            var body = "The ListOfExtensions property cannot be missing or an empty list.";

            // Act
            var mockCall = mockMvc.perform(
                    post(InstructionApi.V1_INSTRUCTION_COMPARE)
                        .headers(InstructionApi.HEADERS_JSON)
                        .content(InstructionApi.JSON_MAPPER.writeValueAsString(compareImageRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

            // Assert
            var content = mockCall.getResponse().getContentAsString();
            var apiResponse = InstructionApi.JSON_MAPPER.readValue(content, ApiResponse.class);
            assertEquals(statusCode, apiResponse.status(), "Request should return HTTP 400 BadRequest.");
            assertEquals(body, apiResponse.body(), "Request should return the BadRequest message for invalid ListOfExtensions.");
        }

        @Test
        public void shouldReturn400BadRequestForInvalidListOfExtensionsEmptyList() throws Exception {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var compareImageRequest = new CompareImageRequest(pathDuplicate, Collections.emptyList(), 0.0);
            var statusCode = 400;
            var body = "The ListOfExtensions property cannot be missing or an empty list.";

            // Act
            var mockCall = mockMvc.perform(
                    post(InstructionApi.V1_INSTRUCTION_COMPARE)
                        .headers(InstructionApi.HEADERS_JSON)
                        .content(InstructionApi.JSON_MAPPER.writeValueAsString(compareImageRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

            // Assert
            var content = mockCall.getResponse().getContentAsString();
            var apiResponse = InstructionApi.JSON_MAPPER.readValue(content, ApiResponse.class);
            assertEquals(statusCode, apiResponse.status(), "Request should return HTTP 400 BadRequest.");
            assertEquals(body, apiResponse.body(), "Request should return the BadRequest message for invalid ListOfExtensions.");
        }

        @Test
        public void shouldReturn400BadRequestForBoundaryLessThanZero() throws Exception {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = -0.5;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);
            var statusCode = 400;
            var body = "The Boundary property must be between 0.0 and 1.0 inclusive.";

            // Act
            var mockCall = mockMvc.perform(
                    post(InstructionApi.V1_INSTRUCTION_COMPARE)
                        .headers(InstructionApi.HEADERS_JSON)
                        .content(InstructionApi.JSON_MAPPER.writeValueAsString(compareImageRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

            // Assert
            var content = mockCall.getResponse().getContentAsString();
            var apiResponse = InstructionApi.JSON_MAPPER.readValue(content, ApiResponse.class);
            assertEquals(statusCode, apiResponse.status(), "Request should return HTTP 400 BadRequest.");
            assertEquals(body, apiResponse.body(), "Request should return the BadRequest message for invalid Boundary.");
        }

        @Test
        public void shouldReturn400BadRequestForBoundaryMoreThanOne() throws Exception {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 1.5;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);
            var statusCode = 400;
            var body = "The Boundary property must be between 0.0 and 1.0 inclusive.";

            // Act
            var mockCall = mockMvc.perform(
                    post(InstructionApi.V1_INSTRUCTION_COMPARE)
                        .headers(InstructionApi.HEADERS_JSON)
                        .content(InstructionApi.JSON_MAPPER.writeValueAsString(compareImageRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

            // Assert
            var content = mockCall.getResponse().getContentAsString();
            var apiResponse = InstructionApi.JSON_MAPPER.readValue(content, ApiResponse.class);
            assertEquals(statusCode, apiResponse.status(), "Request should return HTTP 400 BadRequest.");
            assertEquals(body, apiResponse.body(), "Request should return the BadRequest message for invalid Boundary.");
        }

        @Test
        public void shouldReturn500InternalServerErrorForUnexpectedException() throws Exception {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);
            var statusCode = 500;
            var body = "Internal Server Error";
            when(imageInstructorServiceMock.processRequest(any())).thenThrow(new RuntimeException(body));

            // Act
            var mockCall = mockMvc.perform(
                    post(InstructionApi.V1_INSTRUCTION_COMPARE)
                        .headers(InstructionApi.HEADERS_JSON)
                        .content(InstructionApi.JSON_MAPPER.writeValueAsString(compareImageRequest)))
                .andExpect(status().isInternalServerError())
                .andReturn();

            // Assert
            var content = mockCall.getResponse().getContentAsString();
            var apiResponse = InstructionApi.JSON_MAPPER.readValue(content, ApiResponse.class);
            assertEquals(statusCode, apiResponse.status(), "Request should return HTTP 500 Internal Server Error.");
            assertEquals(body, apiResponse.body(), "Request should return the Internal Server Error message for any exception thrown.");
        }

        @Test
        public void shouldReturn503ServiceUnavailableForConnectionLostException() throws Exception {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);
            var statusCode = 503;
            var body = "Kafka - lost connection";
            when(imageInstructorServiceMock.processRequest(any())).thenThrow(new ConnectionLostException(body));

            // Act
            var mockCall = mockMvc.perform(
                    post(InstructionApi.V1_INSTRUCTION_COMPARE)
                        .headers(InstructionApi.HEADERS_JSON)
                        .content(InstructionApi.JSON_MAPPER.writeValueAsString(compareImageRequest)))
                .andExpect(status().isServiceUnavailable())
                .andReturn();

            // Assert
            var content = mockCall.getResponse().getContentAsString();
            var apiResponse = InstructionApi.JSON_MAPPER.readValue(content, ApiResponse.class);
            assertEquals(statusCode, apiResponse.status(), "Request should return HTTP 503 Service Unavailable.");
            assertEquals(body, apiResponse.body(), "Request should return the Service Unavailable message for a connection exception thrown.");
        }
    }
}
