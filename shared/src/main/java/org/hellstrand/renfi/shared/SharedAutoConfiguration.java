package org.hellstrand.renfi.shared;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableResilientMethods
@EntityScan("org.hellstrand.renfi.shared.persistence.entity")
@EnableJpaRepositories("org.hellstrand.renfi.shared.persistence")
@ComponentScan("org.hellstrand.renfi.shared")
public class SharedAutoConfiguration {
}
