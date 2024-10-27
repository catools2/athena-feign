package org.catools.athena.rest.feign.core.configs;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.catools.athena.core.model.EnvironmentDto;
import org.catools.athena.core.model.ProjectDto;
import org.catools.athena.core.model.VersionDto;
import org.catools.athena.rest.feign.common.configs.ConfigUtils;

@UtilityClass
public class CoreConfigs {

  static {
    reload();
  }

  @Setter
  @Getter
  private static String athenaHost;

  @Setter
  @Getter
  private static String projectName;

  @Setter
  @Getter
  private static String projectCode;

  @Setter
  @Getter
  private static String environmentName;

  @Setter
  @Getter
  private static String environmentCode;

  @Setter
  @Getter
  private static String versionName;

  @Setter
  @Getter
  private static String versionCode;

  @Setter
  @Getter
  private static Integer startAt;

  @Setter
  @Getter
  private static Integer bufferSize;

  @Setter
  @Getter
  private static Integer threadsCount;

  @Setter
  @Getter
  private static Long timeoutInMinutes;

  public static void reload() {
    athenaHost = ConfigUtils.getString("athena.host", "http://localhost:8080/api");
    projectName = ConfigUtils.getString("athena.project.name");
    projectCode = ConfigUtils.getString("athena.project.code");
    environmentName = ConfigUtils.getString("athena.environment.name");
    environmentCode = ConfigUtils.getString("athena.environment.code");
    versionName = ConfigUtils.getString("athena.version.name");
    versionCode = ConfigUtils.getString("athena.version.code");
    startAt = ConfigUtils.getInteger("athena.start_at", 0);
    bufferSize = ConfigUtils.getInteger("athena.buffer_size", 50);
    threadsCount = ConfigUtils.getInteger("athena.threads_count", 10);
    timeoutInMinutes = ConfigUtils.getLong("athena.timeout", 60 * 2L);
  }

  public static ProjectDto getProject() {
    return new ProjectDto(projectCode, projectName);
  }

  public static EnvironmentDto getEnvironment() {
    return new EnvironmentDto(environmentCode, environmentName, projectCode);
  }

  public static VersionDto getVersion() {
    return new VersionDto(versionCode, versionName, projectCode);
  }

}
