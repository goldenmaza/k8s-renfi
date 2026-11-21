package org.hellstrand.renfi.configuration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

@Tag("slow")
@SpringBootTest
@Import(TestContainersConfig.class)
public abstract class BasePersistenceTest {
    @Autowired
    private PostgreSQLContainer<?> postgreSQLContainer;

    @Test
    public void isPostgresTestContainerRunning() {
        // Assert
        assertTrue(postgreSQLContainer.isRunning(), "The PostgreSQL testcontainer should be running.");
    }
}
