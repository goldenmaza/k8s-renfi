package org.hellstrand.renfi.executor;

import java.awt.image.BufferedImage;

public class ImagePixelValidator {
    private static final int MAX_COLOR = 255;
    private static final int RGB_CHANNELS = 3;

    public boolean arePixelsWithinBoundary(final BufferedImage primaryImage, final BufferedImage secondaryImage, final double boundary) {
        final var primaryHeight = primaryImage.getHeight();
        final var primaryWidth = primaryImage.getWidth();
        final var totalPixels = primaryHeight * primaryWidth;

        var pixelDifference = 0;
        for (var y = 0; y < primaryHeight; y++) {
            for (var x = 0; x < primaryWidth; x++) {
                var primaryRGB = primaryImage.getRGB(x, y);
                var secondaryRGB = secondaryImage.getRGB(x, y);

                var redPrimary = (primaryRGB >> 16) & 0xff;
                var greenPrimary = (primaryRGB >> 8) & 0xff;
                var bluePrimary = (primaryRGB) & 0xff;
                var redSecondary = (secondaryRGB >> 16) & 0xff;
                var greenSecondary = (secondaryRGB >> 8) & 0xff;
                var blueSecondary = (secondaryRGB) & 0xff;

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
