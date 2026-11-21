package org.hellstrand.renfi.instructor.request;

import org.hellstrand.renfi.shared.exception.ConnectionLostException;
import org.hellstrand.renfi.shared.model.request.InstructionRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestDispatcher {
    private final RequestSender requestSender;

    public RequestDispatcher(final RequestSender requestSender) {
        this.requestSender = requestSender;
    }

    public boolean dispatch(final String correlationId, final InstructionRequest instructionRequest) {
        try {
            return requestSender.send(correlationId, instructionRequest);
        } catch (RuntimeException e) {
            throw new ConnectionLostException("Infrastructure failure while dispatching.", e);
        }
    }
}
