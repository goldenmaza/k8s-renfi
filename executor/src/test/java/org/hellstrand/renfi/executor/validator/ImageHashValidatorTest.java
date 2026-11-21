package org.hellstrand.renfi.executor.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageHashValidatorTest {
    private final ImageHashValidator imageHashValidator = new ImageHashValidator();

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

        // Act
        var areLikelyDuplicates = imageHashValidator.arePerceptuallySimilar(primaryImage, secondaryImage);

        // Assert
        assertTrue(areLikelyDuplicates, "The images should not match as they have colour differences.");
    }

    @Test
    public void shouldReturnTrueWhenHashMatchAsTheyAreTheSameFile() throws IOException {
        // Arrange
        var primaryImage = ImageIO.read(new File("src/test/resources/compare/images/validateUnique/100x100_symbol.png"));
        var secondaryImage = ImageIO.read(new File("src/test/resources/compare/images/validateUnique/100x100_symbol.png"));

        // Act
        var areLikelyDuplicates = imageHashValidator.arePerceptuallySimilar(primaryImage, secondaryImage);

        // Assert
        assertTrue(areLikelyDuplicates, "The images should match as they are identical of non-solid color, but they didn't.");
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

        // Act
        var areLikelyDuplicates = imageHashValidator.arePerceptuallySimilar(primaryImage, secondaryImage);

        // Assert
        assertFalse(areLikelyDuplicates, "The images should NOT have matched, but they did.");
    }
}
