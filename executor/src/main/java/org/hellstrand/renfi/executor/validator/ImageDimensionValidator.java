package org.hellstrand.renfi.executor.validator;

import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class ImageDimensionValidator {
    public boolean areDimensionsIdentical(final BufferedImage primaryImage, final BufferedImage secondaryImage) {
        return primaryImage.getWidth() == secondaryImage.getWidth()
            && primaryImage.getHeight() == secondaryImage.getHeight();
    }
}
