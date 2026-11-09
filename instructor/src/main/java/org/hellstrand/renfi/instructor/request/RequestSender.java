package org.hellstrand.renfi.instructor.request;

import org.hellstrand.renfi.shared.model.CompareImageRequest;

public interface RequestSender {
    String send(CompareImageRequest compareImageRequest);
}
