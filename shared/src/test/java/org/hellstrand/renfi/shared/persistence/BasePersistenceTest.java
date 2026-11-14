package org.hellstrand.renfi.shared.persistence;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hellstrand.renfi.shared.config.TestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class BasePersistenceTest {
    @Autowired
    private PostgreSQLContainer<?> postgreSQLContainer;

    @Test
    public void isPostgresTestContainerRunning() {
        // Assert
        assertTrue(postgreSQLContainer.isRunning(), "The PostgreSQL testcontainer should be running.");
    }
}
