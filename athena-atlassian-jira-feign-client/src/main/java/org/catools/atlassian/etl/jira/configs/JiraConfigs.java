package org.catools.atlassian.etl.jira.configs;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.catools.athena.rest.feign.common.configs.ConfigUtils;

import java.util.List;

@UtilityClass
public class JiraConfigs {

  @Setter
  @Getter
  private static String jiraHost = ConfigUtils.getString("athena.jira.host");

  @Setter
  @Getter
  private static String jiraUsername = ConfigUtils.getString("athena.jira.username");

  @Setter
  @Getter
  private static String jiraPassword = ConfigUtils.getString("athena.jira.password");

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
  private static Long delayBetweenCallsInMilliseconds = ConfigUtils.getLong("athena.jira.delay_between_calls_in_milliseconds", 1000L);

  @Setter
  @Getter
  private static List<String> issueTypes = ConfigUtils.getStrings("athena.jira.issue_types", List.of("Epic", "Story", "Test", "Bug"));

  @Setter
  @Getter
  private static List<String> fieldsToRead = ConfigUtils.getStrings("athena.jira.fields_to_sync", List.of());

}
