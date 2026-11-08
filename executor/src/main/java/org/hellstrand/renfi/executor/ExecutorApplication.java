package org.hellstrand.renfi.executor;

import org.hellstrand.renfi.executor.processor.ImageComparisonProcessor;
import org.hellstrand.renfi.executor.service.ImageComparisonService;
import org.hellstrand.renfi.executor.validator.ImageDimensionValidator;
import org.hellstrand.renfi.executor.validator.ImageHashValidator;
import org.hellstrand.renfi.executor.validator.ImagePixelValidator;

import java.util.List;

public class ExecutorApplication {
    static void main(String... args) {
        // Verifying that the application can run outside the testing environment... This will be
        // replaced by the Kafka handler that will trigger the flow...
        final var imageDimensionValidator = new ImageDimensionValidator();
        final var imageHashValidator = new ImageHashValidator();
        final var imagePixelValidator = new ImagePixelValidator();
        final var imageComparisonService = new ImageComparisonService(imageDimensionValidator, imageHashValidator, imagePixelValidator);
        final var imageComparisonProcessor = new ImageComparisonProcessor(imageComparisonService);

        final var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
        final var pathUnique = "src/test/resources/compare/images/validateUnique";
        final var listOfExtensions = List.of(".png");
        final var boundary = 0.0;
        var listOfDuplicatePair = imageComparisonProcessor.processDirectory(pathDuplicate, listOfExtensions, boundary);
        System.out.println("Should contain two duplicate pairs: " + listOfDuplicatePair);
        listOfDuplicatePair = imageComparisonProcessor.processDirectory(pathUnique, listOfExtensions, boundary);
        System.out.println("Should contain zero duplicate pairs: " + listOfDuplicatePair);
    }
}
