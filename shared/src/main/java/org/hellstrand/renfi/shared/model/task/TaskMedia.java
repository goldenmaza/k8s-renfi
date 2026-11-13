package org.hellstrand.renfi.shared.model.task;

public enum TaskMedia {
    IMAGE("Image - Handle image files"),
    VIDEO("Video - Handle video files"),
    DOCUMENT("Document - Handle document files");

    private final String label;

    TaskMedia(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
