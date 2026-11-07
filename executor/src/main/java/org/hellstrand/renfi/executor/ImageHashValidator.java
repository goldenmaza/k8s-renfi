package org.hellstrand.renfi.executor;

import java.awt.image.BufferedImage;

public class ImageHashValidator {
    private static final int RGB_CHANNELS = 3;
    private static final int TARGET_HEIGHT = 8;
    private static final int TARGET_WIDTH = 8;
    private static final int TARGET_DIMENSION = TARGET_HEIGHT * TARGET_WIDTH;

    public boolean arePerceptuallySimilar(final BufferedImage primaryImage, final BufferedImage secondaryImage) {
        final var primaryHash = calculateAverageHash(primaryImage);
        final var secondaryHash = calculateAverageHash(secondaryImage);

        return Long.bitCount(primaryHash ^ secondaryHash) <= 5;
    }

    private long calculateAverageHash(final BufferedImage targetImage) {
        final var shrunkImage = shrinkImage(targetImage);
        final var grayscaleMatrix = computeGrayscaleMatrix(shrunkImage);

        if (isSolidColor(grayscaleMatrix)) {
            return computeAverage(grayscaleMatrix);
        }

        return computeHash(grayscaleMatrix, computeAverage(grayscaleMatrix));
    }

    private BufferedImage shrinkImage(final BufferedImage targetImage) {
        final var shrunkImage = new BufferedImage(TARGET_WIDTH, TARGET_HEIGHT, BufferedImage.TYPE_INT_RGB);
        final var shrunkGraphics = shrunkImage.createGraphics();

        shrunkGraphics.drawImage(targetImage, 0, 0, TARGET_WIDTH, TARGET_HEIGHT, null);
        shrunkGraphics.dispose();

        return shrunkImage;
    }

    private int[][] computeGrayscaleMatrix(final BufferedImage shrunkImage) {
        final int[][] grayscale = new int[TARGET_HEIGHT][TARGET_WIDTH];

        for (int y = 0; y < TARGET_HEIGHT; y++) {
            for (int x = 0; x < TARGET_WIDTH; x++) {
                grayscale[x][y] = getGrayscaleValue(shrunkImage.getRGB(x, y));
            }
        }

        return grayscale;
    }

    private int getGrayscaleValue(final int rgb) {
        final var red = (rgb >> 16) & 0xff;
        final var green = (rgb >> 8) & 0xff;
        final var blue = rgb & 0xff;

        return (red + green + blue) / RGB_CHANNELS;
    }

    private boolean isSolidColor(final int[][] grayscaleMatrix) {
        final var firstPixel = grayscaleMatrix[0][0];

        for (final var row : grayscaleMatrix) {
            for (final var pixel : row) {
                if (pixel != firstPixel) {
                    return false;
                }
            }
        }

        return true;
    }

    private int computeAverage(final int[][] grayscaleMatrix) {
        var total = 0;
        var count = 0;
        for (final var row : grayscaleMatrix) {
            for (final var pixel : row) {
                total += pixel;
                count++;
            }
        }

        return count != 0 ? total / TARGET_DIMENSION : 0;
    }

    private long computeHash(final int[][] grayscale, final long average) {
        long hash = 0;
        var bitIndex = 0;
        for (var y = 0; y < TARGET_HEIGHT; y++) {
            for (var x = 0; x < TARGET_WIDTH; x++) {
                if (grayscale[y][x] > average) {
                    hash = setBitInHash(hash, bitIndex);
                }
                bitIndex++;
            }
        }

        return hash;
    }

    private long setBitInHash(final long currentHash, final int index) {
        return currentHash | (1L << index);
    }
}
