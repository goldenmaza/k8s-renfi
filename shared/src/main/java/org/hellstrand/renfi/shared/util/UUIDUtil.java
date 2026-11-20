package org.hellstrand.renfi.shared.util;

import java.util.UUID;
import java.util.regex.Pattern;

public final class UUIDUtil {
    private static final Pattern UUID_LOOSE_PATTERN = Pattern.compile(
        "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    public static UUID generateUUID() {
        return UUID.randomUUID();
    }

    public static boolean validateUUID(final String uuid) {
        return UUID_LOOSE_PATTERN.matcher(uuid).matches();
    }
}
