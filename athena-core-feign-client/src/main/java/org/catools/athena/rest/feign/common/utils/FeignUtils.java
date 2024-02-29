package org.catools.athena.rest.feign.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.*;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Slf4j
@UtilityClass
public class FeignUtils {
  public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  public static final String ENTITY_ID = "entity_id";
  public static final String UTC = "UTC";

  public static <T> T getClient(Class<T> clazz, String host) {
    return getClient(clazz, host, 30, 300);
  }

  public static <T> T getClient(Class<T> clazz, String host, String username, String password) {
    return getClient(clazz, host, username, password, 30, 300);
  }

  public static <T> T getClient(Class<T> clazz, String host, String username, String password, int connectTimeout, int readTimeout) {
    return getClientBuilder(clazz, connectTimeout, readTimeout).requestInterceptor(new BasicAuthRequestInterceptor(username, password))
                                                               .target(clazz, host);
  }

  public static <T> T getClient(Class<T> clazz, String host, int connectTimeout, int readTimeout) {
    return getClientBuilder(clazz, connectTimeout, readTimeout).target(clazz, host);
  }

  private static <T> Feign.Builder getClientBuilder(Class<T> clazz, int connectTimeout, int readTimeout) {
    return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder(objectMapper()))
                .decoder(new JacksonDecoder(objectMapper()))
                .retryer(Retryer.NEVER_RETRY)
                .responseInterceptor((ctx, chain) -> {
                  Response response = ctx.response();
                  if (response.status() >= 300) {
                    throw FeignException.errorStatus(response.request().httpMethod().name(), response);
                  }
                  return chain.next(ctx);
                })
                .logger(new Slf4jLogger(clazz))
                .logLevel(Logger.Level.NONE)
                .options(new Request.Options(connectTimeout, TimeUnit.SECONDS, readTimeout, TimeUnit.SECONDS, true));
  }

  @NotNull
  public static Optional<Long> getEntityId(Response response) {
    try {
      return response.headers().get(ENTITY_ID).stream().findFirst().map(Long::valueOf);
    }
    catch (Exception e) {
      log.error("failed to process request: {}\nresponse: {}", response.request().toString(), response);
      throw e;
    }
  }

  private static ObjectMapper objectMapper() {
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    dateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    objectMapper.setDateFormat(dateFormat);
    objectMapper.registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    return objectMapper;
  }

}
