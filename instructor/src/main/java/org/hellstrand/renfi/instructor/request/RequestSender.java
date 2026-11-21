package org.hellstrand.renfi.instructor.request;

import org.hellstrand.renfi.shared.model.request.InstructionRequest;

public interface RequestSender {
    boolean send(final String correlationId, final InstructionRequest instructionRequest);
}
