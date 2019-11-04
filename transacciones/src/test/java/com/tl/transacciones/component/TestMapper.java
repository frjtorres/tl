package com.tl.transacciones.component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestMapper {

    @Autowired
    private ObjectMapper objectMapper;

    public String mapToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public <T> T mapOneFromJson(String json, Class<T> clazz) throws JsonParseException,
                                                                    JsonMappingException,
                                                                    IOException {
        return objectMapper.readValue(json, clazz);
    }

    public <T> List<T> mapManyFromJson(String json, Class<T> clazz) throws JsonParseException,
                                                                           JsonMappingException,
                                                                           IOException {
        return objectMapper.readValue(json, objectMapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, clazz));
    }
}
