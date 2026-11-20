package org.hellstrand.renfi.shared.model.response;

import org.hellstrand.renfi.shared.model.task.ResultStatus;

import java.util.List;

public record CompareImageResult(
    String correlationId,
    ResultStatus resultStatus,
    List<MatchingPair> listOfMatchingPair,
    long processingTime,
    String failureMessage
) {
}
