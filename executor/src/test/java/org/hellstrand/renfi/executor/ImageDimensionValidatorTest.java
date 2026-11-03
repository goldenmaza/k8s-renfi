package org.hellstrand.renfi.executor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

public class ImageDimensionValidatorTest {
    @Test
    public void shouldReturnTrueWhenDimensionsMatch() {
        // Arrange
        var primaryImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        var secondaryImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        var imageDimensionValidator = new ImageDimensionValidator();

        // Act
        var dimensionsMatch = imageDimensionValidator.compareDimensions(primaryImage, secondaryImage);

        // Assert
        assertTrue(dimensionsMatch, "The images do not have identical dimensions (width & height).");
    }

    @Test
    public void shouldReturnFalseWhenDimensionsDiffer() {
        // Arrange
        var primaryImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        var secondaryImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        var imageDimensionValidator = new ImageDimensionValidator();

        // Act
        var dimensionsMatch = imageDimensionValidator.compareDimensions(primaryImage, secondaryImage);

        // Assert
        assertFalse(dimensionsMatch, "The images do have identical dimensions (width & height), but they shouldn't.");
    }
}
