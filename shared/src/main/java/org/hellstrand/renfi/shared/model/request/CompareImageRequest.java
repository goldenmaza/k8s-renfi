package org.hellstrand.renfi.shared.model.request;

import java.util.List;

public record CompareImageRequest(String directory, List<String> listOfExtensions, double boundary) {
}
