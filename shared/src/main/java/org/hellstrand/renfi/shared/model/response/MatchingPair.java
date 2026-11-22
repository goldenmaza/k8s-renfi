package org.hellstrand.renfi.shared.model.response;

import jakarta.validation.constraints.NotBlank;

public record MatchingPair(
    @NotBlank(message = "The parameter 'PrimaryFilename' is required.")
    String primaryFilename,

    @NotBlank(message = "The parameter 'SecondaryFilename' is required.")
    String secondaryFilename) {
}
