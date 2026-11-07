package org.hellstrand.renfi.executor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImagePixelValidatorTest {
    @Test
    public void shouldReturnTrueWhenPixelsExceedBoundary() {
        // Arrange
        var primaryImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        var primaryGraphics = primaryImage.createGraphics();
        primaryGraphics.setColor(Color.WHITE);
        primaryGraphics.fillRect(0, 0, 100, 100);
        primaryGraphics.dispose();
        var secondaryImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        var secondaryGraphics = secondaryImage.createGraphics();
        secondaryGraphics.setColor(Color.WHITE);
        secondaryGraphics.fillRect(0, 0, 100, 100);
        secondaryGraphics.setColor(Color.ORANGE);
        secondaryGraphics.fillRect(25, 25, 1, 1); // add another box for pixel validation
        secondaryGraphics.dispose();
        var boundary = 0.05;
        var imagePixelValidator = new ImagePixelValidator();

        // Act
        var pixelsMatch = imagePixelValidator.arePixelsWithinBoundary(primaryImage, secondaryImage, boundary);

        // Assert
        assertTrue(pixelsMatch, "The images that are within the boundary should match.");
    }

    @Test
    public void shouldReturnFalseWhenPixelsExceedBoundary() {
        // Arrange
        var primaryImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        var primaryGraphics = primaryImage.createGraphics();
        primaryGraphics.setColor(Color.WHITE);
        primaryGraphics.fillRect(0, 0, 100, 100);
        primaryGraphics.dispose();
        var secondaryImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        var secondaryGraphics = secondaryImage.createGraphics();
        secondaryGraphics.setColor(Color.WHITE);
        secondaryGraphics.fillRect(0, 0, 100, 100);
        secondaryGraphics.setColor(Color.BLACK);
        secondaryGraphics.fillRect(25, 25, 50, 50); // add another box for pixel validation
        secondaryGraphics.dispose();
        var boundary = 0.05;
        var imagePixelValidator = new ImagePixelValidator();

        // Act
        var pixelsMatch = imagePixelValidator.arePixelsWithinBoundary(primaryImage, secondaryImage, boundary);

        // Assert
        assertFalse(pixelsMatch, "The images that are outside the boundary should not match.");
    }
}
