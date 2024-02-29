package org.catools.atlassian.etl.scale;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.rest.feign.tms.clients.TmsClient;
import org.catools.athena.tms.model.TestCycleDto;
import org.catools.atlassian.etl.scale.configs.ScaleConfigs;
import org.catools.atlassian.etl.scale.model.ScaleTestCase;
import org.catools.atlassian.etl.scale.model.ScaleTestRun;
import org.catools.atlassian.etl.scale.rest.cycle.TestRunClient;
import org.catools.atlassian.etl.scale.rest.testcase.TestCaseClient;
import org.catools.atlassian.etl.scale.translators.ScaleTestCaseTranslator;

import java.time.Instant;
import java.util.Date;

import static org.catools.atlassian.etl.scale.translators.ScaleTestRunTranslator.translateTestRun;

@Slf4j
@UtilityClass
public class ScaleSyncClient {

  private final String SYNC_SCALE_RUN = "SYNC_SCALE_RUN";
  private final String SYNC_SCALE_CASE = "SYNC_SCALE_CASE";

  public static void syncTestCases() {
    syncProjectTestCases();
  }

  public static void syncTestRuns() {
    Instant projectSyncStartTime = Instant.now();

    TestRunClient.processTestRuns("createdOn,updatedOn,key", scaleTestRun -> {
      String testRunInfoKey = scaleTestRun.getKey();
      String runDbSyncKey = "SCALE_RUN_" + testRunInfoKey.toUpperCase();

      log.info("Start sync {} run.", testRunInfoKey);
      ScaleTestRun testRun = TestRunClient.getTestRun(testRunInfoKey);
      syncTestRunExecutions(ScaleConfigs.getProjectCode(), testRun);

      TmsClient.saveSyncInfo(ScaleConfigs.getProjectCode(), SYNC_SCALE_RUN, runDbSyncKey, projectSyncStartTime);
      log.info("Finish sync {} run.", testRunInfoKey);
    });
  }

  private void syncProjectTestCases() {
    Instant projectSyncStartTime = Instant.now();

    for (String folderToSync : ScaleConfigs.getTestCasesFoldersToSync()) {
      Date projectLastSync = TmsClient.getLastSync(ScaleConfigs.getProjectCode(), SYNC_SCALE_CASE, folderToSync);

      TestCaseClient.processProjectTestCases(folderToSync, "createdOn,updatedOn,key", testCase -> {
        if (testCase != null && !testCaseIsSynced(projectLastSync, testCase)) {
          log.info("Processing test case {}.", testCase.getKey());
          ScaleTestCase testCaseItem = TestCaseClient.getTestCase(testCase.getKey());
          TmsClient.saveItem(ScaleTestCaseTranslator.translateTestCase(ScaleConfigs.getProjectCode(), testCaseItem));
        }
      });

      TmsClient.saveSyncInfo(ScaleConfigs.getProjectCode(), SYNC_SCALE_CASE, folderToSync, projectSyncStartTime);
    }
  }

  private boolean testCaseIsSynced(Date projectLastSync, ScaleTestCase testcase) {
    if (projectLastSync == null) {
      return false;
    }
    return testcase.getUpdatedOn() != null ? testcase.getUpdatedOn().before(projectLastSync) : testcase.getCreatedOn().before(projectLastSync);
  }

  private void syncTestRunExecutions(String projectKey, ScaleTestRun testRun) {
    log.info("Start updating test run {} with {} execution items.", testRun.getKey(), testRun.getItems().size());
    TestCycleDto cycle = translateTestRun(projectKey, testRun);
    TmsClient.saveTestCycle(cycle);
    log.info("Finish updating test run {} with {} execution items.", testRun.getKey(), testRun.getItems().size());
  }
}
