package org.hellstrand.renfi.instructor.controller;

import org.hellstrand.renfi.instructor.model.ApiFactory;
import org.hellstrand.renfi.instructor.model.ApiResponse;
import org.hellstrand.renfi.instructor.model.InstructionApi;
import org.hellstrand.renfi.instructor.service.ImageInstructorService;
import org.hellstrand.renfi.shared.exception.ConnectionLostException;
import org.hellstrand.renfi.shared.model.CompareImageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(InstructionApi.API_V1)
@RestController
public class ImageInstructorController {
    private final ImageInstructorService imageInstructorService;

    public ImageInstructorController(final ImageInstructorService imageInstructorService) {
        this.imageInstructorService = imageInstructorService;
    }

    @PostMapping(InstructionApi.INSTRUCTION_COMPARE)
    public ResponseEntity<ApiResponse> handleCompareRequest(@RequestBody final CompareImageRequest compareImageRequest) {
        // Schema validation until annotations are added...
        if (compareImageRequest.directory() == null || compareImageRequest.directory().isBlank()) {
            return ApiFactory.badRequest("The Directory property cannot be missing or empty.");
        } // We aren't of course validating that the path could potentially be a real path...

        if (compareImageRequest.listOfExtensions() == null || compareImageRequest.listOfExtensions().isEmpty()) {
            return ApiFactory.badRequest("The ListOfExtensions property cannot be missing or an empty list.");
        } // We aren't of course validating that the ListOfExtensions could potentially be containing real extensions...

        if (compareImageRequest.boundary() < 0.0 || compareImageRequest.boundary() > 1.0) {
            return ApiFactory.badRequest("The Boundary property must be between 0.0 and 1.0 inclusive.");
        }

        try {
            return ApiFactory.accepted(imageInstructorService.processRequest(compareImageRequest));
        } catch (ConnectionLostException e) {
            return ApiFactory.serviceUnavailable(e.getMessage());
        } catch (Exception e) {
            return ApiFactory.serverError("Internal Server Error");
        }
    }
}
