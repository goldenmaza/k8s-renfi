package org.hellstrand.renfi.executor.validator;

import java.awt.image.BufferedImage;

public class ImagePixelValidator {
    private static final int MAX_COLOR = 255;
    private static final int RGB_CHANNELS = 3;

    public boolean arePixelsWithinBoundary(
            final BufferedImage primaryImage, final BufferedImage secondaryImage, final double boundary) {
        final var primaryHeight = primaryImage.getHeight();
        final var primaryWidth = primaryImage.getWidth();
        final var totalPixels = primaryHeight * primaryWidth;

        var pixelDifference = 0;
        for (var y = 0; y < primaryHeight; y++) {
            for (var x = 0; x < primaryWidth; x++) {
                final var primaryRGB = primaryImage.getRGB(x, y);
                final var secondaryRGB = secondaryImage.getRGB(x, y);

                final var redPrimary = (primaryRGB >> 16) & 0xff;
                final var greenPrimary = (primaryRGB >> 8) & 0xff;
                final var bluePrimary = (primaryRGB) & 0xff;
                final var redSecondary = (secondaryRGB >> 16) & 0xff;
                final var greenSecondary = (secondaryRGB >> 8) & 0xff;
                final var blueSecondary = (secondaryRGB) & 0xff;

                pixelDifference += Math.abs(redPrimary - redSecondary);
                pixelDifference += Math.abs(greenPrimary - greenSecondary);
                pixelDifference += Math.abs(bluePrimary - blueSecondary);
            }
        }

        // Determine normalizationFactor for calculating the percentage...
        final var normalizationFactor = totalPixels * MAX_COLOR * RGB_CHANNELS;
        final var percentage = (double) pixelDifference / normalizationFactor;

        return percentage <= boundary;
    }
}
