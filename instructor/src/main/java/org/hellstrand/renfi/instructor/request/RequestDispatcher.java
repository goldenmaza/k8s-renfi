package org.hellstrand.renfi.instructor.request;

import org.hellstrand.renfi.shared.exception.ConnectionLostException;
import org.hellstrand.renfi.shared.model.CompareImageRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestDispatcher {
    private final RequestSender requestSender;

    public RequestDispatcher(final RequestSender requestSender) {
        this.requestSender = requestSender;
    }

    public String dispatch(final CompareImageRequest compareImageRequest) {
        try {
            return requestSender.send(compareImageRequest);
        } catch (RuntimeException e) {
            throw new ConnectionLostException("Infrastructure failure while dispatching.", e);
        }
    }
}
