package org.hellstrand.renfi.shared.model.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CompareImageRequest(
    @NotBlank(message = "The parameter 'Directory' is required.")
    String directory,

    @NotEmpty(message = "The parameter 'ListOfExtensions' is required.")
    List<@NotBlank(message = "An extension cannot be an empty string.") String> listOfExtensions,

    @NotNull(message = "The parameter 'Boundary' is required.")
    @DecimalMin(value = "0.0", message = "The parameter 'Boundary' must be starting from 0.0.")
    @DecimalMax(value = "1.0", message = "The parameter 'Boundary' must be ending at 1.0.")
    double boundary
) implements InstructionRequest {
}
