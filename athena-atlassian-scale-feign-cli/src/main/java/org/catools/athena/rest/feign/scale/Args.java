package org.catools.athena.rest.feign.scale;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.IntegerConverter;
import com.beust.jcommander.converters.LongConverter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.atlassian.etl.scale.configs.ScaleConfigs;
import org.catools.athena.rest.feign.core.configs.CoreConfigs;

import java.util.List;

@Data
public class Args {

  @Parameter(names = {"-ah", "-athena-host"},
      description = "The Athena api endpoint to send information to")
  private String athenaHost;

  @Parameter(names = {"-sh", "-scale-host"},
      description = "The Scale api endpoint to read information from")
  private String scaleHost;

  @Parameter(names = {"-sat", "-scale-access-token"},
      description = "The personal access token to be used for interaction with Scale api")
  private String scaleAccessToken;

  @Parameter(names = {"-su", "-scale-username"},
      description = "The username to be used for interaction with Scale api")
  private String scaleUsername;

  @Parameter(names = {"-sp", "-scale-password"},
      description = "The password to be used for interaction with Scale api")
  private String scalePassword;

  @Parameter(names = {"-pn", "-project-name"},
      description = "The unique project name to use for project identification")
  private String projectName;

  @Parameter(names = {"-pc", "-project-code"},
      description = "The unique project code to use for project identification")
  private String projectCode;

  @Parameter(names = {"-st", "-sync-tests"},
      arity = 1,
      description = "Shall sync tests or not.")
  private Boolean syncTests;

  @Parameter(names = {"-sr", "-sync-runs"},
      arity = 1,
      description = "Shall sync runs or not.")
  private Boolean syncRuns;

  @Parameter(names = {"-s", "-start-at"},
      converter = IntegerConverter.class,
      description = "The index to start query data from Scale.")
  private Integer startAt;

  @Parameter(names = {"-b", "-buffer-size"},
      converter = IntegerConverter.class,
      description = "The buffer size to define maximum number of return value in each Scale call.")
  private Integer bufferSize;

  @Parameter(names = {"-t", "-threads"},
      converter = IntegerConverter.class,
      description = "The number of total threads to use for parallel processing.")
  private Integer threadsCount;

  @Parameter(names = {"-m", "-timeout-in-minutes"},
      converter = LongConverter.class,
      description = "The total amount of wait for sync to be finished.")
  private Long timeoutInMinutes;

  @Parameter(names = {"-trf", "-test-run-folders-to-sync"},
      description = "The test run folders to sync.")
  private List<String> testRunFoldersToSync;

  @Parameter(names = {"-tcf", "-test-case-folders-to-sync"},
      description = "The test case folders to sync.")
  private List<String> testCasesFoldersToSync;

  @Parameter(names = "--help",
      help = true)
  private boolean help = false;

  public void loadConfig() {
    if (StringUtils.isNoneBlank(athenaHost)) {
      CoreConfigs.setAthenaHost(athenaHost);
    }

    if (StringUtils.isNoneBlank(scaleHost)) {
      ScaleConfigs.setScaleHost(scaleHost);
    }

    if (syncTests != null) {
      ScaleConfigs.setSyncTests(syncTests);
    }

    if (syncRuns != null) {
      ScaleConfigs.setSyncRuns(syncRuns);
    }

    if (StringUtils.isNoneBlank(scaleAccessToken)) {
      ScaleConfigs.setScaleAccessToken(scaleAccessToken);
    }

    if (StringUtils.isNoneBlank(scaleUsername)) {
      ScaleConfigs.setScaleUsername(scaleUsername);
    }

    if (StringUtils.isNoneBlank(scalePassword)) {
      ScaleConfigs.setScalePassword(scalePassword);
    }

    if (StringUtils.isNoneBlank(projectName)) {
      CoreConfigs.setProjectName(projectName);
    }

    if (StringUtils.isNoneBlank(projectCode)) {
      CoreConfigs.setProjectCode(projectCode);
    }

    if (startAt != null) {
      CoreConfigs.setStartAt(startAt);
    }

    if (bufferSize != null) {
      CoreConfigs.setBufferSize(bufferSize);
    }

    if (threadsCount != null) {
      CoreConfigs.setThreadsCount(threadsCount);
    }

    if (timeoutInMinutes != null) {
      CoreConfigs.setTimeoutInMinutes(timeoutInMinutes);
    }

    if (testCasesFoldersToSync != null) {
      ScaleConfigs.setTestCasesFoldersToSync(testCasesFoldersToSync);
    }

    if (testRunFoldersToSync != null) {
      ScaleConfigs.setTestRunFoldersToSync(testRunFoldersToSync);
    }
  }
}