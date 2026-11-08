package org.hellstrand.renfi.shared.exception;

public class DirectoryUnavailableException extends ComparisonException {
    public DirectoryUnavailableException(String message) {
        super(message);
    }

    public DirectoryUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
