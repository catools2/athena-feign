package org.catools.athena.rest.feign.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class JsonUtils {

  public static <T> T readValue(final String input, Class<T> clazz) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.findAndRegisterModules();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
      return mapper.readValue(input, clazz);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Nullable
  public static String writeValueAsString(Object parameters) {
    try {
      return parameters == null ? null : new ObjectMapper().writeValueAsString(parameters);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
