package com.asteroids.game.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public interface Dto {
    ObjectMapper objectMapper = new ObjectMapper();

    default String toJsonString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while converting Dto to JSON", e);
        }
    }

    static <DtoType extends Dto> DtoType fromJsonString(String json, Class<DtoType> dtoTypeClass) {
        try {
            return objectMapper.readValue(json, dtoTypeClass);
        } catch (IOException e) {
            throw new RuntimeException("Error while creating Dto from JSON", e);
        }
    }
}
