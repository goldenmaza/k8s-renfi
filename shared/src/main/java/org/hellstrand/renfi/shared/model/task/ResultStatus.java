package org.hellstrand.renfi.shared.model.task;

public enum ResultStatus {
    PROCESSING("Processing - Task is being handled"),
    SUCCESS("Success - Task results are available"),
    FAILED("Failed - Task was unable to complete");

    private final String label;

    ResultStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
