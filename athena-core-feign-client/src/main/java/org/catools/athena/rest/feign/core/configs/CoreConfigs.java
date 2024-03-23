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

  @Setter
  @Getter
  private static String athenaHost = ConfigUtils.getString("athena.host", "http://localhost:8080/api");

  @Setter
  @Getter
  private static String projectName = ConfigUtils.getString("athena.project.name");

  @Setter
  @Getter
  private static String projectCode = ConfigUtils.getString("athena.project.code");

  @Setter
  @Getter
  private static String environmentName = ConfigUtils.getString("athena.environment.name");

  @Setter
  @Getter
  private static String environmentCode = ConfigUtils.getString("athena.environment.code");

  @Setter
  @Getter
  private static String versionName = ConfigUtils.getString("athena.version.name");

  @Setter
  @Getter
  private static String versionCode = ConfigUtils.getString("athena.version.code");

  @Setter
  @Getter
  private static Integer startAt = ConfigUtils.getInteger("athena.start_at", 0);

  @Setter
  @Getter
  private static Integer bufferSize = ConfigUtils.getInteger("athena.buffer_size", 50);

  @Setter
  @Getter
  private static Integer threadsCount = ConfigUtils.getInteger("athena.threads_count", 10);

  @Setter
  @Getter
  private static Long timeoutInMinutes = ConfigUtils.getLong("athena.timeout", 60 * 2L);

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
