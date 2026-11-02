package org.hellstrand.renfi.executor;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hellstrand.renfi.shared.SharedModule;
import org.junit.jupiter.api.Test;

public class ExecutorApplicationTest {
    @Test
    public void contextLoads() {
        SharedModule sm = new SharedModule();
        assertNotNull(sm);
    }
}
