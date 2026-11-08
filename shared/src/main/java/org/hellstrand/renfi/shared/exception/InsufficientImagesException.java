package org.hellstrand.renfi.shared.exception;

public class InsufficientImagesException extends ComparisonException {
    public InsufficientImagesException(final String message) {
        super(message);
    }

    public InsufficientImagesException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
