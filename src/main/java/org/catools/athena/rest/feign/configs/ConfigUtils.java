package org.catools.athena.rest.feign.configs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.core.model.MetadataDto;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class ConfigUtils {

  public static final String CONFIGS_TO_LOAD = "CONFIGS_TO_LOAD";
  private static Config CONFIG;

  static {
    ConfigFactory.invalidateCaches();
    String configToLoad = System.getProperty(CONFIGS_TO_LOAD);
    CONFIG = configToLoad != null ? ConfigFactory.load(configToLoad) : ConfigFactory.load();
  }

  public static Set<MetadataDto> getMetadataList(final String propertyName) {
    return asSet(propertyName, MetadataDto.class);
  }

  public static String getString(final String property) {
    if (isDefined(property))
      return CONFIG.getString(property);

    return System.getProperty(convertToEnvVariable(property));
  }

  public static boolean isDefined(final String property) {
    try {
      return !CONFIG.getIsNull(property);
    } catch (ConfigException ex) {
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
    ObjectMapper mapper = new ObjectMapper();
    mapper.findAndRegisterModules();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
      return mapper.readValue(jsonFormatString, clazz);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
