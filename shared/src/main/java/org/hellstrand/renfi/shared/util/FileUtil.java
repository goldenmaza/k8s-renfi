package org.hellstrand.renfi.shared.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileUtil {
    public static String loadJsonResource(final String directory) {
        try {
            return new String(Files.readAllBytes(Paths.get("src/test/resources/json/tasks/" + directory)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test JSON resource from: " + directory, e);
        }
    }
}
