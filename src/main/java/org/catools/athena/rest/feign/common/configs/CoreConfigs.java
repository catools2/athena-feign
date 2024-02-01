package org.catools.athena.rest.feign.common.configs;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class CoreConfigs {
  public static String getHost() {
    return ConfigUtils.getString("athena.host");
  }

  public static String getProjectCode() {
    return ConfigUtils.getString("athena.project.code");
  }

  public static String getProjectName() {
    return ConfigUtils.getString("athena.project.name");
  }

  public static String getEnvironmentCode() {
    return ConfigUtils.getString("athena.environment.code");
  }

  public static String getEnvironmentName() {
    return ConfigUtils.getString("athena.environment.name");
  }
}
