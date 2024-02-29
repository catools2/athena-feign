package org.catools.atlassian.etl.scale.configs;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.catools.athena.rest.feign.common.configs.ConfigUtils;

import java.util.List;

@UtilityClass
public class ScaleConfigs {

  @Setter
  @Getter
  private static String scaleHost = ConfigUtils.getString("athena.scale.host");

  @Setter
  @Getter
  private static String scaleUsername = ConfigUtils.getString("athena.scale.username");

  @Setter
  @Getter
  private static String scalePassword = ConfigUtils.getString("athena.scale.password");

  @Setter
  @Getter
  private static String projectName = ConfigUtils.getString("athena.project.name");

  @Setter
  @Getter
  private static String projectCode = ConfigUtils.getString("athena.project.code");

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
  private static Long timeoutInMinutes = ConfigUtils.getLong("athena.timeout", 3600L);

  @Setter
  @Getter
  private static Long delayBetweenCallsInMilliseconds = ConfigUtils.getLong("athena.scale.delay_between_calls_in_milliseconds", 1000L);

  @Setter
  @Getter
  private static List<String> testRunFoldersToSync = ConfigUtils.getStrings("athena.scale.test_run_folders_to_sync", List.of("/"));

  @Setter
  @Getter
  private static List<String> testCasesFoldersToSync = ConfigUtils.getStrings("athena.scale.test_cases_folders_to_sync", List.of(""));


  public static String getAtmUri() {
    return scaleHost + "/rest/atm/1.0/";
  }

  public static String getTestsUri() {
    return scaleHost + "/rest/tests/1.0/";
  }

}
