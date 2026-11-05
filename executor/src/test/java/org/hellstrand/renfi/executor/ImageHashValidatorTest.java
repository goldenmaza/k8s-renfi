package org.hellstrand.renfi.executor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageHashValidatorTest {
    @Test
    public void shouldReturnTrueWhenHashMatch() {
        // Arrange
        var primaryImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        var primaryGaphics = primaryImage.createGraphics();
        primaryGaphics.setColor(Color.WHITE);
        primaryGaphics.fillRect(0, 0, 100, 100);
        primaryGaphics.fillRect(10, 10, 5, 5); // add another box to increase the bit count
        primaryGaphics.dispose();
        var secondaryImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        var secondaryGaphics = secondaryImage.createGraphics();
        secondaryGaphics.setColor(Color.WHITE);
        secondaryGaphics.fillRect(0, 0, 100, 100);
        secondaryGaphics.fillRect(11, 11, 5, 5); // add another box to increase the bit count
        secondaryGaphics.dispose();
        var imageHashValidator = new ImageHashValidator();

        // Act
        var hashMatch = imageHashValidator.compareHash(primaryImage, secondaryImage);

        // Assert
        assertTrue(hashMatch, "The images should not match as they have colour differences.");
    }

    @Test
    public void shouldReturnFalseWhenHashDiffer() {
        // Arrange
        var primaryImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        var primaryGaphics = primaryImage.createGraphics();
        primaryGaphics.setColor(Color.WHITE);
        primaryGaphics.fillRect(0, 0, 100, 100);
        primaryGaphics.dispose();
        var secondaryImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        var secondaryGaphics = secondaryImage.createGraphics();
        secondaryGaphics.setColor(Color.BLACK);
        secondaryGaphics.fillRect(0, 0, 100, 100);
        secondaryGaphics.dispose();
        var imageHashValidator = new ImageHashValidator();

        // Act
        var hashMatch = imageHashValidator.compareHash(primaryImage, secondaryImage);

        // Assert
        assertFalse(hashMatch, "The images should NOT have matched, but they did.");
    }
}
