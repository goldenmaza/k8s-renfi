package org.hellstrand.renfi.instructor.controller;

import org.hellstrand.renfi.instructor.model.ApiResponse;
import org.hellstrand.renfi.instructor.service.ImageInstructorService;
import org.hellstrand.renfi.shared.exception.ConnectionLostException;
import org.hellstrand.renfi.shared.model.CompareImageRequest;

public class ImageInstructorController {
    private final ImageInstructorService imageInstructorService;

    public ImageInstructorController(final ImageInstructorService imageInstructorService) {
        this.imageInstructorService = imageInstructorService;
    }

    public ApiResponse handleCompareRequest(final CompareImageRequest compareImageRequest) {
        // Schema validation until annotations are added...
        if (compareImageRequest.directory() == null || compareImageRequest.directory().isBlank()) {
            return new ApiResponse(400, "The Directory property cannot be missing or empty.");
        } // We aren't of course validating that the path could potentially be a real path...

        if (compareImageRequest.listOfExtensions() == null || compareImageRequest.listOfExtensions().isEmpty()) {
            return new ApiResponse(400, "The ListOfExtensions property cannot be missing or an empty list.");
        } // We aren't of course validating that the ListOfExtensions could potentially be containing real extensions...

        if (compareImageRequest.boundary() < 0.0 || compareImageRequest.boundary() > 1.0) {
            return new ApiResponse(400, "The Boundary property must be between 0.0 and 1.0 inclusive.");
        }

        try {
            return new ApiResponse(202, imageInstructorService.processRequest(compareImageRequest));
        } catch (ConnectionLostException e) {
            return new ApiResponse(503, e.getMessage());
        } catch (Exception e) {
            return new ApiResponse(500, "Internal Server Error");
        }
    }
}
