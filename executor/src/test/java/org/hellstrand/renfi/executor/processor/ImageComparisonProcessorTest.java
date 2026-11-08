package org.hellstrand.renfi.executor.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hellstrand.renfi.executor.service.ImageComparisonService;
import org.hellstrand.renfi.executor.validator.ImageDimensionValidator;
import org.hellstrand.renfi.executor.validator.ImageHashValidator;
import org.hellstrand.renfi.executor.validator.ImagePixelValidator;
import org.hellstrand.renfi.shared.exception.DirectoryUnavailableException;
import org.hellstrand.renfi.shared.exception.InsufficientImagesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ImageComparisonProcessorTest {
    private ImageDimensionValidator imageDimensionValidator;
    private ImageHashValidator imageHashValidator;
    private ImagePixelValidator imagePixelValidator;
    private ImageComparisonService imageComparisonService;
    private ImageComparisonProcessor imageComparisonProcessor;

    @BeforeEach
    public void setUp() {
        imageDimensionValidator = new ImageDimensionValidator();
        imageHashValidator = new ImageHashValidator();
        imagePixelValidator = new ImagePixelValidator();
        imageComparisonService = new ImageComparisonService(imageDimensionValidator, imageHashValidator, imagePixelValidator);
        imageComparisonProcessor = new ImageComparisonProcessor(imageComparisonService);
    }

    @Nested
    public class ProcessorOKCases {
        @Test
        public void shouldReturnZeroAsThereAreNoDuplicatesInDirectory() {
            // Arrange
            var path = "src/test/resources/compare/images/validateUnique";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;

            // Act
            var listOfDuplicatePair = imageComparisonProcessor.processDirectory(path, listOfExtensions, boundary);

            // Assert
            assertEquals(0, listOfDuplicatePair.size(), "The processor should have found no duplicates, but it didn't.");
        }

        @Test
        public void shouldReturnTwoWhenDirectoryHasTwoDuplicateImages() {
            // Arrange
            var path = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;

            // Act
            var listOfDuplicatePair = imageComparisonProcessor.processDirectory(path, listOfExtensions, boundary);

            // Assert
            assertEquals(2, listOfDuplicatePair.size(), "The processor should have found two duplicate pairs, but it didn't.");
        }
    }

    @Nested
    public class ProcessorExceptionCases {
        @Test
        public void shouldThrowExceptionWhenDirectoryIsMissing() {
            // Arrange
            var path = "src/test/resource/compare/images/validateUnique"; // s is missing in resources
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;

            // Act & Assert
            assertThrows(DirectoryUnavailableException.class, () -> imageComparisonProcessor.processDirectory(path, listOfExtensions, boundary));
        }

        @Test
        public void shouldThrowExceptionWhenDirectoryIsEmpty() {
            // Arrange
            var path = "src/test/resources/compare"; // compare folder holding just a directory
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;

            // Act & Assert
            assertThrows(InsufficientImagesException.class, () -> imageComparisonProcessor.processDirectory(path, listOfExtensions, boundary));
        }
    }
}
