package org.hellstrand.renfi.shared.model.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskEnumTest {
    private final Reflections reflections = new Reflections(this.getClass().getPackageName());

    @Test
    public void validateEnumSymmetry() {
        // Arrange & Act
        Set<Class<? extends Enum>> discoveredEnums = reflections.getSubTypesOf(Enum.class);
        Set<String> approvedEnums = new HashSet<>(List.of(
            TaskType.class.getSimpleName(),
            TaskMedia.class.getSimpleName(),
            TaskStatus.class.getSimpleName()
        ));
        Set<String> currentEnums = discoveredEnums.stream().map(Class::getSimpleName).collect(Collectors.toSet());

        // Assert
        assertEquals(approvedEnums, currentEnums, "Symmetry mismatch: Enums are missing from the approved registry.");
    }

    @Test
    public void validateEnumContent() {
        // Arrange
        Map<String, Enum<?>[]> enums = new HashMap<>() {{
            put(TaskType.class.toString(), TaskType.values());
            put(TaskMedia.class.toString(), TaskType.values());
            put(TaskStatus.class.toString(), TaskType.values());
        }};

        // Act & Assert
        for (Map.Entry<String, Enum<?>[]> entry : enums.entrySet()) {
            assertThat(entry.getValue()).isNotEmpty();
        }
    }
}
