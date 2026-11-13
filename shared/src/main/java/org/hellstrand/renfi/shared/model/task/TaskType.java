package org.hellstrand.renfi.shared.model.task;

public enum TaskType {
    COMPARE("Compare - Task is to compare files under directory"),
    CROP("Crop - Task is to crop files under directory"),
    RESIZE("Resize - Task is to resize files under directory");

    private final String label;

    TaskType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
