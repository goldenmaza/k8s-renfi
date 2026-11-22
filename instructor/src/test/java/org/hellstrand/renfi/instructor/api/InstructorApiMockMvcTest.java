package org.hellstrand.renfi.instructor.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hellstrand.renfi.configuration.BaseSpringContextTest;
import org.hellstrand.renfi.instructor.model.ApiResponse;
import org.hellstrand.renfi.instructor.model.InstructionApi;
import org.hellstrand.renfi.instructor.request.RequestDispatcher;
import org.hellstrand.renfi.instructor.request.RequestSender;
import org.hellstrand.renfi.instructor.service.ImageInstructorService;
import org.hellstrand.renfi.shared.exception.ConnectionLostException;
import org.hellstrand.renfi.shared.model.request.CompareImageRequest;
import org.hellstrand.renfi.shared.persistence.TaskRepository;
import org.hellstrand.renfi.shared.util.UUIDUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

@AutoConfigureMockMvc
public class InstructorApiMockMvcTest extends BaseSpringContextTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private ImageInstructorService imageInstructorServiceSpy;

    @MockitoSpyBean
    private TaskRepository taskRepositorySpy;

    @MockitoSpyBean
    private RequestDispatcher requestDispatcherSpy;

    @MockitoBean
    private RequestSender requestSender;

    @Nested
    public class MockOKCases {
        @Test
        public void shouldCallInstructionCompareEndpointAndExpect200Response() throws Exception {
            // Arrange
            var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;
            var compareImageRequest = new CompareImageRequest(pathDuplicate, listOfExtensions, boundary);

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
            assertTrue(UUIDUtil.validateUUID(apiResponse.body()), "The service must return the exact correlation ID provided by the dispatcher.");
            verify(imageInstructorServiceSpy, times(1)).processRequest(any());
            verify(taskRepositorySpy, times(1)).save(any());
            verify(requestDispatcherSpy, times(1)).dispatch(any(), any());
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
            verify(imageInstructorServiceSpy, times(0)).processRequest(any());
            verify(taskRepositorySpy, times(0)).save(any());
            verify(requestDispatcherSpy, times(0)).dispatch(any(), any());
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
            verify(imageInstructorServiceSpy, times(0)).processRequest(any());
            verify(taskRepositorySpy, times(0)).save(any());
            verify(requestDispatcherSpy, times(0)).dispatch(any(), any());
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
            verify(imageInstructorServiceSpy, times(0)).processRequest(any());
            verify(taskRepositorySpy, times(0)).save(any());
            verify(requestDispatcherSpy, times(0)).dispatch(any(), any());
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
            verify(imageInstructorServiceSpy, times(0)).processRequest(any());
            verify(taskRepositorySpy, times(0)).save(any());
            verify(requestDispatcherSpy, times(0)).dispatch(any(), any());
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
            verify(imageInstructorServiceSpy, times(0)).processRequest(any());
            verify(taskRepositorySpy, times(0)).save(any());
            verify(requestDispatcherSpy, times(0)).dispatch(any(), any());
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
            verify(imageInstructorServiceSpy, times(0)).processRequest(any());
            verify(taskRepositorySpy, times(0)).save(any());
            verify(requestDispatcherSpy, times(0)).dispatch(any(), any());
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
            doThrow(new RuntimeException(body)).when(imageInstructorServiceSpy).processRequest(any());

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
            verify(imageInstructorServiceSpy, times(1)).processRequest(any());
            verify(taskRepositorySpy, times(0)).save(any());
            verify(requestDispatcherSpy, times(0)).dispatch(any(), any());
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
            doThrow(new ConnectionLostException(body)).when(imageInstructorServiceSpy).processRequest(any());

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
            verify(imageInstructorServiceSpy, times(1)).processRequest(any());
            verify(taskRepositorySpy, times(0)).save(any());
            verify(requestDispatcherSpy, times(0)).dispatch(any(), any());
        }
    }
}
