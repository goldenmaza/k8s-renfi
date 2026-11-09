package org.hellstrand.renfi.instructor.service;

import org.hellstrand.renfi.instructor.request.RequestDispatcher;
import org.hellstrand.renfi.shared.model.CompareImageRequest;

public class ImageInstructorService {
    private final RequestDispatcher requestDispatcher;

    public ImageInstructorService(final RequestDispatcher requestDispatcher) {
        this.requestDispatcher = requestDispatcher;
    }

    public String processRequest(final CompareImageRequest compareImageRequest) {
        return requestDispatcher.dispatch(compareImageRequest);
    }
}
