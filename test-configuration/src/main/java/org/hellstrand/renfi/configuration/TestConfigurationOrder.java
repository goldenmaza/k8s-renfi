package org.hellstrand.renfi.configuration;

import org.junit.jupiter.api.ClassDescriptor;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.ClassOrdererContext;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;

public class TestConfigurationOrder implements ClassOrderer {
    @Override
    public void orderClasses(ClassOrdererContext context) {
        context.getClassDescriptors().sort(Comparator.comparing(this::isHeavyTest));
    }

    private boolean isHeavyTest(ClassDescriptor descriptor) {
        return descriptor.findAnnotation(SpringBootTest.class).isPresent();
    }
}
