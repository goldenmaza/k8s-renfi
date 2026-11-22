package org.hellstrand.renfi.executor.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hellstrand.renfi.configuration.BaseSpringContextTest;
import org.hellstrand.renfi.executor.service.ImageComparisonService;
import org.hellstrand.renfi.executor.validator.ImageDimensionValidator;
import org.hellstrand.renfi.executor.validator.ImageHashValidator;
import org.hellstrand.renfi.executor.validator.ImagePixelValidator;
import org.hellstrand.renfi.shared.exception.DirectoryUnavailableException;
import org.hellstrand.renfi.shared.exception.InsufficientImagesException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ImageComparisonProcessorTest extends BaseSpringContextTest {
    @Autowired
    private ImageDimensionValidator imageDimensionValidator;

    @Autowired
    private ImageHashValidator imageHashValidator;

    @Autowired
    private ImagePixelValidator imagePixelValidator;

    @Autowired
    private ImageComparisonService imageComparisonService;

    @Autowired
    private ImageComparisonProcessor imageComparisonProcessor;

    @Nested
    public class ProcessorOKCases {
        @Test
        public void shouldReturnZeroAsThereAreNoMatchingInDirectory() {
            // Arrange
            var path = "src/test/resources/compare/images/validateUnique";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;

            // Act
            var listOfMatchingPair = imageComparisonProcessor.processDirectory(path, listOfExtensions, boundary);

            // Assert
            assertEquals(0, listOfMatchingPair.size(), "The processor should have found no matches, but it did.");
        }

        @Test
        public void shouldReturnTwoWhenDirectoryHasTwoMatchingPairs() {
            // Arrange
            var path = "src/test/resources/compare/images/validateDuplicate";
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;

            // Act
            var listOfMatchingPair = imageComparisonProcessor.processDirectory(path, listOfExtensions, boundary);

            // Assert
            assertEquals(2, listOfMatchingPair.size(), "The processor should have found two matching pairs, but it didn't.");
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
            var path = "/compare"; // compare holding just a directory
            var listOfExtensions = List.of(".png");
            var boundary = 0.0;

            // Act & Assert
            assertThrows(InsufficientImagesException.class, () -> imageComparisonProcessor.processDirectory(path, listOfExtensions, boundary));
        }
    }
}
