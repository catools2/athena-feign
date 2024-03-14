package org.catools.athena.atlassian.etl.scale.configs;

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
  private static String scaleAccessToken = ConfigUtils.getString("athena.scale.access_token");

  @Setter
  @Getter
  private static String scaleUsername = ConfigUtils.getString("athena.scale.username");

  @Setter
  @Getter
  private static String scalePassword = ConfigUtils.getString("athena.scale.password");

  @Setter
  @Getter
  private static Long delayBetweenCallsInMilliseconds = ConfigUtils.getLong("athena.scale.delay_between_calls_in_milliseconds", 1000L);

  @Setter
  @Getter
  private static List<String> testRunFoldersToSync = ConfigUtils.getStrings("athena.scale.test_run_folders_to_sync", List.of("/"));

  @Setter
  @Getter
  private static List<String> testCasesFoldersToSync = ConfigUtils.getStrings("athena.scale.test_cases_folders_to_sync", List.of(""));

  @Setter
  @Getter
  private static boolean syncTests = ConfigUtils.getBoolean("athena.scale.sync_tests", true);

  @Setter
  @Getter
  private static boolean syncRuns = ConfigUtils.getBoolean("athena.scale.sync_runs", true);

  public static String getAtmUri() {
    return scaleHost + "/rest/atm/1.0/";
  }

  public static String getTestsUri() {
    return scaleHost + "/rest/tests/1.0/";
  }

}
