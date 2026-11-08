package org.hellstrand.renfi.shared.exception;

public class ComparisonException extends RuntimeException {
    public ComparisonException(final String message) {
        super(message);
    }

    public ComparisonException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
