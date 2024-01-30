package org.catools.athena.rest.feign.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.catools.athena.rest.feign.configs.CoreConfigs;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class FeignClient {
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
