package ru.tinkoff.edu.java.scrapper.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class ObjectMapperUtil {

    private ObjectMapperUtil() {
    }

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    public static <T> T readValue(String json, Class<T> valueType) {
        try {
            return OBJECT_MAPPER.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String writeValueAsString(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
