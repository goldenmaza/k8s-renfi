package org.hellstrand.renfi.executor.service;

import org.hellstrand.renfi.executor.validator.ImageDimensionValidator;
import org.hellstrand.renfi.executor.validator.ImageHashValidator;
import org.hellstrand.renfi.executor.validator.ImagePixelValidator;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class ImageComparisonService {
    private final ImageDimensionValidator imageDimensionValidator;
    private final ImageHashValidator imageHashValidator;
    private final ImagePixelValidator imagePixelValidator;

    public ImageComparisonService(
            final ImageDimensionValidator imageDimensionValidator,
            final ImageHashValidator imageHashValidator,
            final ImagePixelValidator imagePixelValidator) {
        this.imageDimensionValidator = imageDimensionValidator;
        this.imageHashValidator = imageHashValidator;
        this.imagePixelValidator = imagePixelValidator;
    }

    public boolean areImagesMatching(
            final BufferedImage primaryImage, final BufferedImage secondaryImage, final double boundary) {
        if (!imageDimensionValidator.areDimensionsIdentical(primaryImage, secondaryImage)) {
            return false;
        }

        if (!imageHashValidator.arePerceptuallySimilar(primaryImage, secondaryImage)) {
            return false;
        }

        return imagePixelValidator.arePixelsWithinBoundary(primaryImage, secondaryImage, boundary);
    }
}
