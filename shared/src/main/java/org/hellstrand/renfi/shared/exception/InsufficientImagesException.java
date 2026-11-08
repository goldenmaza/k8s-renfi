package org.hellstrand.renfi.shared.exception;

public class InsufficientImagesException extends ComparisonException {
    public InsufficientImagesException(String message) {
        super(message);
    }

    public InsufficientImagesException(String message, Throwable cause) {
        super(message, cause);
    }
}
