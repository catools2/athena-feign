package org.catools.athena.rest.feign.pipeline.utils;

import feign.FeignException;
import lombok.experimental.UtilityClass;
import org.catools.athena.pipeline.model.PipelineDto;
import org.catools.athena.rest.feign.core.configs.CoreConfigs;
import org.catools.athena.rest.feign.pipeline.clients.*;

import java.util.Optional;

import static org.catools.athena.rest.feign.common.utils.FeignUtils.getClient;

@UtilityClass
public class PipelineUtils {

  public static Optional<PipelineDto> getPipeline(final String pipelineName, final String pipelineNumber, final String environmentCode) {
    try {
      return Optional.ofNullable(getClient(PipelineClient.class, CoreConfigs.getAthenaHost()).getPipeline(pipelineName,
          pipelineNumber,
          environmentCode));
    }
    catch (FeignException.NotFound ex) {
      return Optional.empty();
    }
  }

  public static ExecutionStatusClient getExecutionStatusClient() {
    return getClient(ExecutionStatusClient.class, CoreConfigs.getAthenaHost());
  }

  public static PipelineClient getPipelineClient() {
    return getClient(PipelineClient.class, CoreConfigs.getAthenaHost());
  }

  public static ExecutionClient getExecutionClient() {
    return getClient(ExecutionClient.class, CoreConfigs.getAthenaHost());
  }

  public static ScenarioExecutionClient getScenarioExecutionClient() {
    return getClient(ScenarioExecutionClient.class, CoreConfigs.getAthenaHost());
  }
}
