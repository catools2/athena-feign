package org.catools.athena.rest.feign.common.configs;

import com.typesafe.config.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.core.model.MetadataDto;
import org.catools.athena.rest.feign.common.utils.JsonUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Slf4j
public class ConfigUtils {

  private static Config CONFIG;

  static {
    ConfigFactory.invalidateCaches();
    CONFIG = ConfigFactory.load();
  }

  public static Set<MetadataDto> getMetadataSet(final String propertyName) {
    return asSet(propertyName, MetadataDto.class);
  }

  public static Integer getInteger(final String property, Integer defaultValue) {
    if (isDefined(property)) {
      return CONFIG.getInt(property);
    }

    String envValue = System.getProperty(convertToEnvVariable(property));
    return envValue == null ? defaultValue : Integer.valueOf(envValue);
  }

  public static Integer getInteger(final String property) {
    return getInteger(property, null);
  }

  public static Long getLong(final String property, Long defaultValue) {
    if (isDefined(property)) {
      return CONFIG.getLong(property);
    }

    String envValue = System.getProperty(convertToEnvVariable(property));
    return envValue == null ? defaultValue : Long.valueOf(envValue);
  }

  public static Long getLong(final String property) {
    return getLong(property, null);
  }

  public static String getString(final String property, String defaultValue) {
    if (isDefined(property)) {
      return CONFIG.getString(property);
    }

    return StringUtils.defaultIfBlank(System.getProperty(convertToEnvVariable(property)), defaultValue);
  }

  public static String getString(final String property) {
    return getString(property, null);
  }

  public static List<String> getStrings(final String property, List<String> defaultValue) {
    if (isDefined(property)) {
      return CONFIG.getStringList(property);
    }

    return defaultValue;
  }

  public static List<String> getStrings(final String property) {
    return getStrings(property, List.of());
  }

  public static Boolean getBoolean(final String property, Boolean defaultValue) {
    if (isDefined(property)) {
      return CONFIG.getBoolean(property);
    }

    String prop = System.getProperty(convertToEnvVariable(property));
    return StringUtils.isBlank(prop) ? defaultValue : Boolean.parseBoolean(prop);
  }

  public static Boolean getBoolean(final String property) {
    return getBoolean(property, null);
  }

  public static boolean isDefined(final String property) {
    try {
      return !CONFIG.getIsNull(property);
    }
    catch (ConfigException ex) {
      return false;
    }
  }

  public <T> T asModel(final String property, final Class<T> clazz) {
    Config val = CONFIG.getConfig(property);
    return getModelFromConfig(clazz, val);
  }

  public static <T> Set<T> asSet(final String property, final Class<T> clazz) {
    Set<T> output = new HashSet<>();
    List<? extends Config> configs = CONFIG.getConfigList(property);
    for (Config val : configs) {
      output.add(getModelFromConfig(clazz, val));
    }
    return output;
  }

  @NotNull
  private static String convertToEnvVariable(final String property) {
    return property.replaceAll("\\.", "_").toUpperCase();
  }

  private static <T> T getModelFromConfig(Class<T> clazz, Config val) {
    String jsonFormatString = val.resolve().root().render(ConfigRenderOptions.concise());
    return JsonUtils.readValue(jsonFormatString, clazz);
  }
}
