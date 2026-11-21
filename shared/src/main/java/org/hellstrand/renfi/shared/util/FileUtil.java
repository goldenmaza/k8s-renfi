package org.hellstrand.renfi.shared.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class FileUtil {
    public static File resolveDirectory(final String directoryPath) {
        var normalized = directoryPath
            .replaceFirst("^/?src/test/resources/", "")
            .replaceFirst("^/", "");

        var resource = FileUtil.class.getResource("/" + normalized);
        if (resource != null) {
            return new File(resource.getPath());
        }

        return new File(directoryPath);
    }

    public static String loadJsonResource(final String filename) {
        try {
            var inputStreamOfResource = Objects.requireNonNull(FileUtil.class.getResourceAsStream("/json/tasks/" + filename));
            return new String(inputStreamOfResource.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test JSON resource from: " + filename, e);
        }
    }
}
