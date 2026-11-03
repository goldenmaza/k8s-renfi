package org.hellstrand.renfi.executor;

import java.awt.image.BufferedImage;

public class ImageDimensionValidator {
    public boolean compareDimensions(BufferedImage primaryImage, BufferedImage secondaryImage) {
        return primaryImage.getWidth() == secondaryImage.getWidth()
            && primaryImage.getHeight() == secondaryImage.getHeight();
    }
}
