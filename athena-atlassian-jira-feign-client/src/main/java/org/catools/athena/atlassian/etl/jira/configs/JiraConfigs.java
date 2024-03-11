package org.catools.athena.atlassian.etl.jira.configs;

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
  private static String jiraAccessToken = ConfigUtils.getString("athena.jira.access_token");

  @Setter
  @Getter
  private static String jiraUsername = ConfigUtils.getString("athena.jira.username");

  @Setter
  @Getter
  private static String jiraPassword = ConfigUtils.getString("athena.jira.password");

  @Setter
  @Getter
  private static Long delayBetweenCallsInMilliseconds = ConfigUtils.getLong("athena.jira.delay_between_calls_in_milliseconds", 1000L);

  @Setter
  @Getter
  private static List<String> issueTypes = ConfigUtils.getStrings("athena.jira.issue_types", List.of("Epic", "Story", "Test", "Bug"));

  @Setter
  @Getter
  private static List<String> fieldsToRead = ConfigUtils.getStrings("athena.jira.fields_to_sync",
      List.of("Affected Version",
          "Affected Version/s",
          "Component",
          "Functional Area (PX)",
          "IssueLink",
          "Label",
          "Team"));

}
