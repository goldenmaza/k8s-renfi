package org.hellstrand.renfi.executor.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hellstrand.renfi.executor.validator.ImageDimensionValidator;
import org.hellstrand.renfi.executor.validator.ImageHashValidator;
import org.hellstrand.renfi.executor.validator.ImagePixelValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

public class ImageComparisonServiceTest {
    private ImageComparisonService imageComparisonService;
    private ImageDimensionValidator imageDimensionValidatorMock;
    private ImageHashValidator imageHashValidatorMock;
    private ImagePixelValidator imagePixelValidatorMock;

    @BeforeEach
    public void setUp() {
        imageDimensionValidatorMock = mock(ImageDimensionValidator.class);
        imageHashValidatorMock = mock(ImageHashValidator.class);
        imagePixelValidatorMock = mock(ImagePixelValidator.class);
        imageComparisonService = new ImageComparisonService(imageDimensionValidatorMock, imageHashValidatorMock, imagePixelValidatorMock);
    }

    @Test
    public void shouldReturnTrueWhenAllValidatorsPass() {
        // Arrange
        var primaryImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        var secondaryImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        var boundary = 100.0;
        when(imageDimensionValidatorMock.areDimensionsIdentical(any(), any())).thenReturn(true);
        when(imageHashValidatorMock.arePerceptuallySimilar(any(), any())).thenReturn(true);
        when(imagePixelValidatorMock.arePixelsWithinBoundary(any(), any(), anyDouble())).thenReturn(true);

        // Act
        var imagesAreDuplicates = imageComparisonService.areImagesDuplicates(primaryImage, secondaryImage, boundary);

        // Assert
        assertTrue(imagesAreDuplicates, "The images that passed all validators should be seen as duplicates.");
        verify(imageDimensionValidatorMock, times(1)).areDimensionsIdentical(any(), any());
        verify(imageHashValidatorMock, times(1)).arePerceptuallySimilar(any(), any());
        verify(imagePixelValidatorMock, times(1)).arePixelsWithinBoundary(any(), any(), anyDouble());
    }

    @Test
    public void shouldReturnFalseWhenDimensionsDiffer() {
        // Arrange
        when(imageDimensionValidatorMock.areDimensionsIdentical(any(), any())).thenReturn(false);

        // Act
        var imagesAreDuplicates = imageComparisonService.areImagesDuplicates(null, null, 0.0);

        // Assert
        assertFalse(imagesAreDuplicates, "The images do appear to be duplicates by their dimensions, but they shouldn't.");
        verify(imageDimensionValidatorMock, times(1)).areDimensionsIdentical(any(), any());
        verify(imageHashValidatorMock, never()).arePerceptuallySimilar(any(), any());
        verify(imagePixelValidatorMock, never()).arePixelsWithinBoundary(any(), any(), anyDouble());
    }

    @Test
    public void shouldReturnFalseWhenHashDiffer() {
        // Arrange
        when(imageDimensionValidatorMock.areDimensionsIdentical(any(), any())).thenReturn(true);
        when(imageHashValidatorMock.arePerceptuallySimilar(any(), any())).thenReturn(false);

        // Act
        var imagesArePerceptuallySimilar = imageComparisonService.areImagesDuplicates(null, null, 0.0);

        // Assert
        assertFalse(imagesArePerceptuallySimilar, "The images do appear to be duplicates by their hash, but they shouldn't.");
        verify(imageDimensionValidatorMock, times(1)).areDimensionsIdentical(any(), any());
        verify(imageHashValidatorMock, times(1)).arePerceptuallySimilar(any(), any());
        verify(imagePixelValidatorMock, never()).arePixelsWithinBoundary(any(), any(), anyDouble());
    }

    @Test
    public void shouldReturnFalseWhenPixelsDiffer() {
        // Arrange
        when(imageDimensionValidatorMock.areDimensionsIdentical(any(), any())).thenReturn(true);
        when(imageHashValidatorMock.arePerceptuallySimilar(any(), any())).thenReturn(true);
        when(imagePixelValidatorMock.arePixelsWithinBoundary(any(), any(), anyDouble())).thenReturn(false);

        // Act
        var imagesArePerceptuallySimilar = imageComparisonService.areImagesDuplicates(null, null, 0.0);

        // Assert
        assertFalse(imagesArePerceptuallySimilar, "The images do appear to be duplicates by their pixels, but they shouldn't.");
        verify(imageDimensionValidatorMock, times(1)).areDimensionsIdentical(any(), any());
        verify(imageHashValidatorMock, times(1)).arePerceptuallySimilar(any(), any());
        verify(imagePixelValidatorMock, times(1)).arePixelsWithinBoundary(any(), any(), anyDouble());
    }
}
