package org.hellstrand.renfi.shared;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(SharedAutoConfiguration.class)
@SpringBootApplication
public class SharedContextApplication {
}
