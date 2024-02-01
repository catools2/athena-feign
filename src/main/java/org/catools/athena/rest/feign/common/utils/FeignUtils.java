package org.catools.athena.rest.feign.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Feign;
import feign.Logger;
import feign.Response;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.experimental.UtilityClass;
import org.catools.athena.rest.feign.common.configs.CoreConfigs;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.TimeZone;

@UtilityClass
public class FeignUtils {
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

  public static <T> T getClient(Class<T> clazz) {
    return Feign.builder()
        .client(new OkHttpClient())
        .encoder(new JacksonEncoder(objectMapper()))
        .decoder(new JacksonDecoder(objectMapper()))
        .logger(new Slf4jLogger(clazz))
        .logLevel(Logger.Level.FULL)
        .target(clazz, CoreConfigs.getHost());
  }

  @NotNull
  public static Optional<Long> getEntityId(Response response) {
    return response.headers().get("entity_id").stream().findFirst().map(Long::valueOf);
  }

  private static ObjectMapper objectMapper() {
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setDateFormat(dateFormat);
    objectMapper.registerModule(new JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    return objectMapper;
  }

}
