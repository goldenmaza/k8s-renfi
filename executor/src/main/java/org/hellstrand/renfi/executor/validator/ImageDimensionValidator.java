package org.hellstrand.renfi.executor.validator;

import java.awt.image.BufferedImage;

public class ImageDimensionValidator {
    public boolean areDimensionsIdentical(BufferedImage primaryImage, BufferedImage secondaryImage) {
        return primaryImage.getWidth() == secondaryImage.getWidth()
            && primaryImage.getHeight() == secondaryImage.getHeight();
    }
}
