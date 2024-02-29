package org.catools.atlassian.etl.jira;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.core.model.ProjectDto;
import org.catools.athena.rest.feign.core.client.CoreClient;
import org.catools.athena.rest.feign.tms.clients.TmsClient;
import org.catools.atlassian.etl.jira.client.AthenaJiraClient;
import org.catools.atlassian.etl.jira.configs.JiraConfigs;
import org.catools.atlassian.etl.jira.translators.JiraTranslator;

import java.time.Instant;
import java.util.Date;

@Slf4j
@UtilityClass
public class JiraSyncClient {
  private final String SYNC_JIRA = "SYNC_JIRA";

  public void syncJira() {
    CoreClient.getProject(new ProjectDto(JiraConfigs.getProjectCode(), JiraConfigs.getProjectName()));
    Instant syncStartTime = Instant.now();

    for (String issueType : JiraConfigs.getIssueTypes()) {
      addItems(JiraConfigs.getProjectCode(), issueType);
    }

    TmsClient.saveSyncInfo(JiraConfigs.getProjectCode(), SYNC_JIRA, "PROJECT", syncStartTime);
  }

  public void addItems(String projectKey, String issueType) {

    Date lastSync = TmsClient.getLastSync(projectKey, SYNC_JIRA, issueType);

    Instant syncStartTime = Instant.now();
    AthenaJiraClient.processIssues(projectKey, issueType, lastSync, issues -> {
      log.info("Process jira {} {}", issueType, issues.getKey());
      TmsClient.saveItem(JiraTranslator.translateIssue(issues, projectKey, JiraConfigs.getFieldsToRead()));
    });

    TmsClient.saveSyncInfo(projectKey, SYNC_JIRA, issueType, syncStartTime);
  }

}
