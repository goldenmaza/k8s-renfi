package org.hellstrand.renfi.shared.model.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hellstrand.renfi.shared.model.task.ResultStatus;

import java.util.List;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "taskType"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CompareImageResult.class, name = "compare")
})
public sealed interface InstructionResult permits CompareImageResult {
    String correlationId();
    ResultStatus resultStatus();
    List<MatchingPair> listOfMatchingPair();
    long processingTime();
    String failureMessage();
}
