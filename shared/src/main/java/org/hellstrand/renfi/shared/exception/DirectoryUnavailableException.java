package org.hellstrand.renfi.shared.exception;

public class DirectoryUnavailableException extends ComparisonException {
    public DirectoryUnavailableException(final String message) {
        super(message);
    }

    public DirectoryUnavailableException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
