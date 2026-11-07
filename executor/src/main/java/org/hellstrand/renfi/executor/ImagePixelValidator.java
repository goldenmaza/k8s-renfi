package org.hellstrand.renfi.executor;

import java.awt.image.BufferedImage;

public class ImagePixelValidator {
    public boolean arePixelsWithinBoundary(BufferedImage primaryImage, BufferedImage secondaryImage, double boundary) {
        var primaryHeight = primaryImage.getHeight();
        var primaryWidth = primaryImage.getWidth();

        var pixelDifference = 0;
        var totalPixels = primaryHeight * primaryWidth;
        var maxColourIntensity = 255 * 3; // max colour per pixel * R + G + B
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

        // Determine layeredPixels for calculating the percentage...
        var layeredPixels = totalPixels * maxColourIntensity;
        var percentageDifference = (double) pixelDifference / layeredPixels;

        return percentageDifference <= boundary;
    }
}
