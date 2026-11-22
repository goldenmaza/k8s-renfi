package org.hellstrand.renfi.shared.model.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hellstrand.renfi.shared.model.task.ResultStatus;

import java.util.List;

public record CompareImageResult(
    @NotBlank(message = "The parameter 'CorrelationId' is required.")
    String correlationId,

    @NotNull(message = "The parameter 'ResultStatus' is required.")
    ResultStatus resultStatus,

    @NotNull(message = "The parameter 'ListOfMatchingPair' is required.")
    List<@Valid @NotNull(message = "The item 'MatchingPair' is required.") MatchingPair> listOfMatchingPair,

    @NotNull(message = "The parameter 'ProcessingTime' is required.")
    @Positive(message = "The parameter 'ProcessingTime' should be a positive number.")
    long processingTime,

    @NotBlank(message = "The parameter 'FailureMessage' is required.")
    String failureMessage
) implements InstructionResult {
}
