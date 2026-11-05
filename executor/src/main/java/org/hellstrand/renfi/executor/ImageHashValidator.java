package org.hellstrand.renfi.executor;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageHashValidator {
    private static final short TARGET_HEIGHT = 8;
    private static final short TARGET_WIDTH = 8;
    private static final int TARGET_DIMENSION = TARGET_HEIGHT * TARGET_WIDTH;

    public boolean arePerceptuallySimilar(BufferedImage primaryImage, BufferedImage secondaryImage) {
        // Calculate average hash values...
        long primaryHash = calculateAverageHash(primaryImage);
        long secondaryHash = calculateAverageHash(secondaryImage);

        // Determine the difference in bits...
        return Long.bitCount(primaryHash ^ secondaryHash) <= 5;
    }

    private long calculateAverageHash(BufferedImage targetImage) {
        // Shrink the targetImage to ignore details...
        var shrunkImage = shrinkImage(targetImage);

        // Convert to grayscale...
        var grayscaleMatrix = computeGrayscaleMatrix(shrunkImage);

        // Solid colour check...
        if (isSolidColour(grayscaleMatrix)) {
            return computeAverage(grayscaleMatrix);
        }

        // Create the 64-bit hash...
        return buildHash(grayscaleMatrix, computeAverage(grayscaleMatrix));
    }

    private BufferedImage shrinkImage(BufferedImage targetImage) {
        BufferedImage shrunkImage = new BufferedImage(TARGET_WIDTH, TARGET_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D bufferedGraphics = shrunkImage.createGraphics();
        bufferedGraphics.drawImage(targetImage, 0, 0, TARGET_WIDTH, TARGET_HEIGHT, null);
        bufferedGraphics.dispose();
        return shrunkImage;
    }

    private int[][] computeGrayscaleMatrix(BufferedImage shrunkImage) {
        int[][] grayscale = new int[TARGET_HEIGHT][TARGET_WIDTH];
        for (int y = 0; y < TARGET_HEIGHT; y++) {
            for (int x = 0; x < TARGET_WIDTH; x++) {
                grayscale[x][y] = getGrayscaleValue(shrunkImage.getRGB(x, y));
            }
        }
        return grayscale;
    }

    private int getGrayscaleValue(int rgb) {
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        return (r + g + b) / 3;
    }

    private boolean isSolidColour(int[][] grayscaleMatrix) {
        int firstPixel = grayscaleMatrix[0][0];
        for (int[] row : grayscaleMatrix) {
            for (int pixel : row) {
                if (pixel != firstPixel) {
                    return false;
                }
            }
        }
        return true;
    }

    private int computeAverage(int[][] grayscaleMatrix) {
        int total = 0, count = 0;
        for (int[] row : grayscaleMatrix) {
            for (int pixel : row) {
                total += pixel;
                count++;
            }
        }
        return count != 0 ? total / TARGET_DIMENSION : 0;
    }

    private long buildHash(int[][] grayscale, long avg) {
        long hash = 0;
        int bitIndex = 0;
        for (int y = 0; y < TARGET_HEIGHT; y++) {
            for (int x = 0; x < TARGET_WIDTH; x++) {
                if (grayscale[y][x] > avg) {
                    hash = setBitInHash(hash, bitIndex);
                }
                bitIndex++;
            }
        }
        return hash;
    }

    private long setBitInHash(long currentHash, int index) {
        return currentHash | (1L << index);
    }
}
