package org.hellstrand.renfi.shared.model.task;

public enum TaskStatus {
    PENDING("Pending - Task is waiting"),
    PROCESSING("Processing - Task is being handled"),
    COMPLETED("Completed - Task results are available"),
    FAILED("Failed - Task was unable to complete");

    private final String label;

    TaskStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
