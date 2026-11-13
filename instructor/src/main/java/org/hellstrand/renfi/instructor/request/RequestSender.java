package org.hellstrand.renfi.instructor.request;

import org.hellstrand.renfi.shared.model.request.CompareImageRequest;

public interface RequestSender {
    String send(final CompareImageRequest compareImageRequest);
}
