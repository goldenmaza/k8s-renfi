package org.hellstrand.renfi.instructor;

import org.hellstrand.renfi.shared.SharedAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(SharedAutoConfiguration.class)
@SpringBootApplication
public class InstructorApplication {
    static void main(String... args) {
        SpringApplication.run(InstructorApplication.class, args);
    }
}
