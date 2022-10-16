package com.productboard.productboardrepos;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Centralized functionality assures json conversion
 */
public final class JsonMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonMapper() {
    }

    public static JsonNode convertToJsonNode(String input) {
        try {
            return OBJECT_MAPPER.readTree(input.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
