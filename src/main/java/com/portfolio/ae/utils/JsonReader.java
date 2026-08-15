package com.portfolio.ae.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Reads JSON fixtures from {@code src/test/resources/testdata/} (PLAN.md section 7: stable
 * reference data — known products, seed user credentials, expected texts).
 */
public final class JsonReader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonReader() {
    }

    public static <T> T readValue(String classpathResource, Class<T> type) {
        try (InputStream input = openResource(classpathResource)) {
            return OBJECT_MAPPER.readValue(input, type);
        } catch (IOException e) {
            throw new IllegalStateException("Error parsing '" + classpathResource + "' as " + type.getSimpleName(), e);
        }
    }

    public static <T> List<T> readList(String classpathResource, TypeReference<List<T>> typeReference) {
        try (InputStream input = openResource(classpathResource)) {
            return OBJECT_MAPPER.readValue(input, typeReference);
        } catch (IOException e) {
            throw new IllegalStateException("Error parsing '" + classpathResource + "' as a list", e);
        }
    }

    public static Map<String, String> readStringMap(String classpathResource) {
        try (InputStream input = openResource(classpathResource)) {
            return OBJECT_MAPPER.readValue(input, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            throw new IllegalStateException("Error parsing '" + classpathResource + "' as a string map", e);
        }
    }

    private static InputStream openResource(String classpathResource) {
        InputStream input = JsonReader.class.getClassLoader().getResourceAsStream(classpathResource);
        if (input == null) {
            throw new IllegalStateException("Could not find fixture '" + classpathResource + "' on the classpath");
        }
        return input;
    }
}
