package org.catools.atlassian.etl.scale.rest.cycle;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.rest.feign.common.utils.FeignUtils;
import org.catools.atlassian.etl.scale.client.ScaleAtmClient;
import org.catools.atlassian.etl.scale.configs.ScaleConfigs;
import org.catools.atlassian.etl.scale.model.ScaleTestRun;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.catools.athena.rest.feign.common.utils.ThreadUtils.executeInParallel;
import static org.catools.athena.rest.feign.common.utils.ThreadUtils.sleep;

@Slf4j
@UtilityClass
public class TestRunClient {

  private final ScaleAtmClient SCALE_ATM_CLIENT = FeignUtils.getClient(ScaleAtmClient.class,
      ScaleConfigs.getAtmUri(),
      ScaleConfigs.getScaleUsername(),
      ScaleConfigs.getScalePassword());

  public void processTestRuns(String fieldsToRead, Consumer<ScaleTestRun> onAction) {

    AtomicInteger counter = new AtomicInteger(0);

    executeInParallel(ScaleConfigs.getThreadsCount(), ScaleConfigs.getTimeoutInMinutes(), () -> {
      while (true) {
        int startFrom = counter.getAndIncrement() * ScaleConfigs.getBufferSize() + ScaleConfigs.getStartAt();
        log.info("Process test runs from {} to {}", startFrom, startFrom + ScaleConfigs.getBufferSize());
        Set<ScaleTestRun> testRuns = _getAllTestRuns(startFrom, fieldsToRead);
        if (testRuns == null || testRuns.isEmpty()) {
          return true;
        }
        testRuns.forEach(onAction);
      }
    });
  }

  private Set<ScaleTestRun> _getAllTestRuns(int startAt, String fieldsToRead) {
    sleep(ScaleConfigs.getDelayBetweenCallsInMilliseconds());
    log.debug("All Test Runs, projectKey: {}, fields: {}, startAT: {}, maxResult: {}",
        ScaleConfigs.getProjectCode(),
        fieldsToRead,
        startAt,
        ScaleConfigs.getBufferSize());

    if (StringUtils.isEmpty(fieldsToRead)) {
      return SCALE_ATM_CLIENT.searchTestRun(startAt,
          ScaleConfigs.getBufferSize(),
          String.format("projectKey = \"%s\" AND folder = \"%s\"", ScaleConfigs.getProjectCode(), ScaleConfigs.getTestRunFoldersToSync()));
    }

    return SCALE_ATM_CLIENT.searchTestRun(startAt,
        ScaleConfigs.getBufferSize(),
        fieldsToRead,
        String.format("projectKey = \"%s\" AND folder = \"%s\"", ScaleConfigs.getProjectCode(), ScaleConfigs.getTestRunFoldersToSync()));
  }

  public ScaleTestRun getTestRun(String testRunKey) {
    sleep(ScaleConfigs.getDelayBetweenCallsInMilliseconds());
    return SCALE_ATM_CLIENT.getTestRun(testRunKey);
  }
}
