package org.hellstrand.renfi.shared.exception;

public class DispatcherException extends RuntimeException {
    public DispatcherException(final String message) {
        super(message);
    }

    public DispatcherException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
