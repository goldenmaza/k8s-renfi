package org.hellstrand.renfi.executor;

import org.hellstrand.renfi.executor.processor.ImageComparisonProcessor;
import org.hellstrand.renfi.executor.service.ImageComparisonService;
import org.hellstrand.renfi.executor.validator.ImageDimensionValidator;
import org.hellstrand.renfi.executor.validator.ImageHashValidator;
import org.hellstrand.renfi.executor.validator.ImagePixelValidator;
import org.hellstrand.renfi.shared.model.DuplicatePair;

import java.util.List;

public class ExecutorApplication {
    static void main(String... args) {
        // Verifying that the application can run outside the testing environment... This will be
        // replaced by the Kafka handler that will trigger the flow...
        var imageDimensionValidator = new ImageDimensionValidator();
        var imageHashValidator = new ImageHashValidator();
        var imagePixelValidator = new ImagePixelValidator();
        var imageComparisonService = new ImageComparisonService(imageDimensionValidator, imageHashValidator, imagePixelValidator);
        var imageComparisonProcessor = new ImageComparisonProcessor(imageComparisonService);

        var pathDuplicate = "src/test/resources/compare/images/validateDuplicate";
        var pathUnique = "src/test/resources/compare/images/validateUnique";
        var listOfExtensions = List.of(".png");
        var boundary = 0.0;
        List<DuplicatePair> listOfDuplicatePair = imageComparisonProcessor.processDirectory(pathDuplicate, listOfExtensions, boundary);
        System.out.println("Should contain two duplicate pairs: " + listOfDuplicatePair);
        listOfDuplicatePair = imageComparisonProcessor.processDirectory(pathUnique, listOfExtensions, boundary);
        System.out.println("Should contain zero duplicate pairs: " + listOfDuplicatePair);
    }
}
