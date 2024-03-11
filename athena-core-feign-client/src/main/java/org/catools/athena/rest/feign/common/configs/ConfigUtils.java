package org.catools.athena.rest.feign.common.configs;

import com.google.common.collect.Sets;
import com.typesafe.config.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.core.model.MetadataDto;
import org.catools.athena.rest.feign.common.utils.JsonUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

@Slf4j
public class ConfigUtils {

  private static Config CONFIG;

  static {
    reload();
  }

  public static final String CONFIGS_TO_LOAD = "CONFIGS_TO_LOAD";

  /**
   * Load configuration and set them in System.properties
   */
  public synchronized static void reload() {
    ConfigFactory.invalidateCaches();
    String configToLoad = System.getProperty(CONFIGS_TO_LOAD);
    CONFIG = configToLoad != null ? ConfigFactory.load(configToLoad) : ConfigFactory.load();
    getUserDefinedSettings().forEach(entry -> {
      String key = entry.getKey();
      if (key.toLowerCase().startsWith("athena")) {
        String propName = pathToEnvVariableName(key);
        if (System.getProperty(propName) == null) {
          System.setProperty(propName, CONFIG.getValue(key).unwrapped().toString());
        }
      }
    });
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

  public static Long getLong(final String property, Long defaultValue) {
    if (isDefined(property)) {
      return CONFIG.getLong(property);
    }

    String envValue = System.getProperty(convertToEnvVariable(property));
    return envValue == null ? defaultValue : Long.valueOf(envValue);
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

    String prop = System.getProperty(convertToEnvVariable(property));
    return StringUtils.isBlank(prop) ? defaultValue : JsonUtils.readValue(prop, ArrayList.class);
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

  public static boolean isDefined(final String property) {
    try {
      return !CONFIG.getIsNull(property);
    }
    catch (ConfigException ex) {
      return false;
    }
  }

  public static  <T> T asModel(final String property, final Class<T> clazz) {
    if (isDefined(property)) {
      Config val = CONFIG.getConfig(property);
      return getModelFromConfig(clazz, val);
    }

    String prop = System.getProperty(convertToEnvVariable(property));
    return StringUtils.isBlank(prop) ? null : JsonUtils.readValue(prop, clazz);
  }

  public static <T> Set<T> asSet(final String property, final Class<T> clazz) {
    if (isDefined(property)) {
      Set<T> output = new HashSet<>();
      List<? extends Config> configs = CONFIG.getConfigList(property);
      for (Config val : configs) {
        output.add(getModelFromConfig(clazz, val));
      }
      return output;
    }

    String prop = System.getProperty(convertToEnvVariable(property));
    return StringUtils.isBlank(prop) ? Sets.newHashSet() : JsonUtils.readValue(prop, HashSet.class);
  }

  @NotNull
  private static String convertToEnvVariable(final String property) {
    return property.replaceAll("\\.", "_").toUpperCase();
  }

  private static <T> T getModelFromConfig(Class<T> clazz, Config val) {
    String jsonFormatString = val.resolve().root().render(ConfigRenderOptions.concise());
    return JsonUtils.readValue(jsonFormatString, clazz);
  }

  private static String pathToEnvVariableName(String path) {
    return path.toUpperCase().replaceAll("[^a-zA-Z0-9]+", "_");
  }

  private static Stream<Map.Entry<String, ConfigValue>> getUserDefinedSettings() {
    return CONFIG.entrySet().stream().filter(entry -> entry.getValue().origin().resource() != null);
  }
}
