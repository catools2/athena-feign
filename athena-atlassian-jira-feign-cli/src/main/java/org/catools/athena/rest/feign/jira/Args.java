package org.catools.athena.rest.feign.jira;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.IntegerConverter;
import com.beust.jcommander.converters.LongConverter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.atlassian.etl.jira.configs.JiraConfigs;
import org.catools.athena.rest.feign.core.configs.CoreConfigs;

import java.util.List;

@Data
public class Args {

  @Parameter(names = {"-ah", "-athena-host"},
      description = "The Athena api endpoint to send information to")
  private String athenaHost;

  @Parameter(names = {"-jh", "-jira-host"},
      description = "The Jira api endpoint to read information from")
  private String jiraHost;

  @Parameter(names = {"-jat", "-jira-access-token"},
      description = "The personal access token to be used for interaction with Jira api")
  private String jiraAccessToken;

  @Parameter(names = {"-ju", "-jira-username"},
      description = "The username to be used for interaction with Jira api")
  private String jiraUsername;

  @Parameter(names = {"-jp", "-jira-password"},
      description = "The password to be used for interaction with Jira api")
  private String jiraPassword;

  @Parameter(names = {"-pn", "-project-name"},
      description = "The unique project name to use for project identification")
  private String projectName;

  @Parameter(names = {"-pc", "-project-code"},
      description = "The unique project code to use for project identification")
  private String projectCode;

  @Parameter(names = {"-i", "-issue-type"},
      description = "The issue type to sync")
  private List<String> issueTypes;

  @Parameter(names = {"-f", "-fields_to_sync"},
      description = "The list of Jira fields to read during sync.")
  private List<String> fieldsToRead;

  @Parameter(names = {"-s", "-start-at"},
      converter = IntegerConverter.class,
      description = "The index to start query data from Jira.")
  private Integer startAt;

  @Parameter(names = {"-b", "-buffer-size"},
      converter = IntegerConverter.class,
      description = "The buffer size to define maximum number of return value in each Jira call.")
  private Integer bufferSize;

  @Parameter(names = {"-t", "-threads"},
      converter = IntegerConverter.class,
      description = "The number of total threads to use for parallel processing.")
  private Integer threadsCount;

  @Parameter(names = {"-m", "-timeout-in-minutes"},
      converter = LongConverter.class,
      description = "The total amount of wait for sync to be finished.")
  private Long timeoutInMinutes;

  @Parameter(names = "--help",
      help = true)
  private boolean help = false;

  public void loadConfig() {
    if (StringUtils.isNoneBlank(athenaHost)) {
      CoreConfigs.setAthenaHost(athenaHost);
    }

    if (StringUtils.isNoneBlank(jiraHost)) {
      JiraConfigs.setJiraHost(jiraHost);
    }

    if (StringUtils.isNoneBlank(jiraAccessToken)) {
      JiraConfigs.setJiraAccessToken(jiraAccessToken);
    }

    if (StringUtils.isNoneBlank(jiraUsername)) {
      JiraConfigs.setJiraUsername(jiraUsername);
    }

    if (StringUtils.isNoneBlank(jiraPassword)) {
      JiraConfigs.setJiraPassword(jiraPassword);
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

    if (fieldsToRead != null) {
      JiraConfigs.setFieldsToRead(fieldsToRead);
    }

    if (issueTypes != null) {
      JiraConfigs.setIssueTypes(issueTypes);
    }
  }
}