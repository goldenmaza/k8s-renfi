package org.hellstrand.renfi.shared.model.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "taskType"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CompareImageRequest.class, name = "compare")
})
public sealed interface InstructionRequest permits CompareImageRequest {
    String directory();
    List<String> listOfExtensions();
    double boundary();
}
