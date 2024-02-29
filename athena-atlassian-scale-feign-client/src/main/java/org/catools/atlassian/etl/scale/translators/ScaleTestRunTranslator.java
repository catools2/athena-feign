package org.catools.atlassian.etl.scale.translators;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.tms.model.TestCycleDto;
import org.catools.athena.tms.model.TestExecutionDto;
import org.catools.atlassian.etl.scale.model.ScaleTestExecution;
import org.catools.atlassian.etl.scale.model.ScaleTestRun;

import java.util.Objects;

import static org.catools.atlassian.etl.scale.translators.TranslatorHelper.*;

@Slf4j
public class ScaleTestRunTranslator {
  public static TestCycleDto translateTestRun(String projectKey, ScaleTestRun testRun) {
    log.info("Start translating test run {} with {} execution items.", testRun.getKey(), testRun.getItems().size());

    Objects.requireNonNull(testRun);

    String folder = "";

    if (StringUtils.isNotBlank(testRun.getFolder())) {
      folder = testRun.getFolder().trim();
      if (!folder.endsWith("/")) {
        folder += "/";
      }
    }

    TestCycleDto etlCycle = new TestCycleDto();
    etlCycle.setCode(testRun.getKey());

    etlCycle.setVersion(getVersion(testRun.getVersion(), projectKey));
    etlCycle.setName(folder + testRun.getName());
    etlCycle.setEndDate(testRun.getPlannedEndDate() == null ? null : testRun.getPlannedEndDate().toInstant());
    etlCycle.setStartDate(testRun.getPlannedStartDate() == null ? null : testRun.getPlannedStartDate().toInstant());

    for (ScaleTestExecution item : testRun.getItems()) {
      etlCycle.getTestExecutions().add(translateExecution(testRun, item));
    }

    log.info("Finish translating test run {} with {} execution items.", testRun.getKey(), testRun.getItems().size());

    return etlCycle;
  }

  private static TestExecutionDto translateExecution(ScaleTestRun testRun, ScaleTestExecution execution) {
    Objects.requireNonNull(execution);

    TestExecutionDto etlExecution = new TestExecutionDto();

    etlExecution.setItem(execution.getTestCaseKey());
    etlExecution.setStatus(getStatus(execution.getStatus() == null ? "CREATED" : execution.getStatus().name()));
    etlExecution.setExecutor(getUser(execution.getExecutedBy()));
    etlExecution.setCreatedOn(testRun.getCreatedOn() == null ? null : testRun.getCreatedOn().toInstant());
    etlExecution.setExecutedOn(execution.getExecutionDate() == null ? null : execution.getExecutionDate().toInstant());

    return etlExecution;
  }

}
