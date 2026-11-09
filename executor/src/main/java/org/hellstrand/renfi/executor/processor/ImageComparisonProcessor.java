package org.hellstrand.renfi.executor.processor;

import static java.lang.String.format;

import org.hellstrand.renfi.executor.service.ImageComparisonService;
import org.hellstrand.renfi.shared.exception.DirectoryUnavailableException;
import org.hellstrand.renfi.shared.exception.InsufficientImagesException;
import org.hellstrand.renfi.shared.model.DuplicatePair;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageComparisonProcessor {
    private final ImageComparisonService imageComparisonService;

    public ImageComparisonProcessor(final ImageComparisonService imageComparisonService) {
        this.imageComparisonService = imageComparisonService;
    }

    public List<DuplicatePair> processDirectory(final String path, final List<String> extensions, final double boundary) {
        Objects.requireNonNull(path, "The directory path stated cannot be null.");
        Objects.requireNonNull(extensions, "The list of extensions cannot be null.");
        final var listOfDuplicates = new ArrayList<DuplicatePair>();
        final var files = loadFiles(path, extensions);

        for (int i = 0; i < files.length; i++) {
            for (int j = i + 1; j < files.length; j++) {
                try {
                    final var primaryImage = ImageIO.read(files[i]);
                    final var secondaryImage = ImageIO.read(files[j]);
                    if (imageComparisonService.areImagesDuplicates(primaryImage, secondaryImage, boundary)) {
                        listOfDuplicates.add(new DuplicatePair(files[i].getName(), files[j].getName()));
                    }
                } catch (IOException e) {
                    // Just print for now, logging and database handling will be implemented later...
                    System.err.println("Error reading files: " + e.getMessage());
                }
            }
        }

        return listOfDuplicates;
    }

    private File[] loadFiles(final String path, final List<String> extensions) {
        final var directory = new File(path);
        if (!directory.exists() && !directory.isDirectory()) {
            throw new DirectoryUnavailableException(format("No directory found with the stated path: %s", path));
        }

        final var files = directory.listFiles((_, f) ->
            extensions.stream().anyMatch(e -> f.toLowerCase().endsWith(e))
        );
        if (files == null || files.length < 2) {
            throw new InsufficientImagesException(format("Not enough media files found in the stated directory: %s", path));
        }

        return files;
    }
}
