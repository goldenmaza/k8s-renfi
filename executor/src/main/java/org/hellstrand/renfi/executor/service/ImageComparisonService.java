package org.hellstrand.renfi.executor.service;

import org.hellstrand.renfi.executor.validator.ImageDimensionValidator;
import org.hellstrand.renfi.executor.validator.ImageHashValidator;
import org.hellstrand.renfi.executor.validator.ImagePixelValidator;

import java.awt.image.BufferedImage;

public class ImageComparisonService {
    private final ImageDimensionValidator imageDimensionValidator;
    private final ImageHashValidator imageHashValidator;
    private final ImagePixelValidator imagePixelValidator;

    public ImageComparisonService(
            ImageDimensionValidator imageDimensionValidator,
            ImageHashValidator imageHashValidator,
            ImagePixelValidator imagePixelValidator) {
        this.imageDimensionValidator = imageDimensionValidator;
        this.imageHashValidator = imageHashValidator;
        this.imagePixelValidator = imagePixelValidator;
    }

    public boolean areImagesDuplicates(BufferedImage primaryImage, BufferedImage secondaryImage, double boundary) {
        if (!imageDimensionValidator.areDimensionsIdentical(primaryImage, secondaryImage)) {
            return false;
        }

        if (!imageHashValidator.arePerceptuallySimilar(primaryImage, secondaryImage)) {
            return false;
        }

        return imagePixelValidator.arePixelsWithinBoundary(primaryImage, secondaryImage, boundary);
    }
}
