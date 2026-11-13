package org.hellstrand.renfi.instructor.model;

import org.springframework.http.ResponseEntity;

public final class ApiFactory {
    private static ResponseEntity<ApiResponse> createResponse(final int status, final String body) {
        return ResponseEntity.status(status).body(new ApiResponse(status, body));
    }

    public static ResponseEntity<ApiResponse> accepted(final String correlationId) {
        return createResponse(202, correlationId);
    }

    public static ResponseEntity<ApiResponse> badRequest(final String message) {
        return createResponse(400, message);
    }

    public static ResponseEntity<ApiResponse> serverError(final String message) {
        return createResponse(500, message);
    }

    public static ResponseEntity<ApiResponse> serviceUnavailable(final String message) {
        return createResponse(503, message);
    }
}
