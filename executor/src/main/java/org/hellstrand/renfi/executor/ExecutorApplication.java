package org.hellstrand.renfi.executor;

import org.hellstrand.renfi.shared.SharedAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(SharedAutoConfiguration.class)
@SpringBootApplication
public class ExecutorApplication {
    static void main(String... args) {
        SpringApplication.run(ExecutorApplication.class, args);
    }
}
