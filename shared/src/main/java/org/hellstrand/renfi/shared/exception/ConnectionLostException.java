package org.hellstrand.renfi.shared.exception;

public class ConnectionLostException extends DispatcherException {
    public ConnectionLostException(final String message) {
        super(message);
    }

    public ConnectionLostException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
