package org.hellstrand.renfi.instructor.model;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;

public final class InstructionApi {
    public static final JsonMapper JSON_MAPPER = new JsonMapper();

    public static final HttpHeaders HEADERS_JSON = new HttpHeaders(MultiValueMap.fromSingleValue(
            Map.of(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

    public static final String API_V1 = "/v1";
    public static final String INSTRUCTION_COMPARE = "/instruction/compare";
    public static final String V1_INSTRUCTION_COMPARE = API_V1 + INSTRUCTION_COMPARE;
}
