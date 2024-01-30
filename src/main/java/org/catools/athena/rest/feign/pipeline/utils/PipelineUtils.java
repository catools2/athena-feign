package org.catools.athena.rest.feign.pipeline.utils;

import feign.Feign;
import feign.FeignException;
import feign.Logger;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.experimental.UtilityClass;
import org.catools.athena.pipeline.model.PipelineDto;
import org.catools.athena.rest.feign.client.FeignClient;
import org.catools.athena.rest.feign.configs.CoreConfigs;
import org.catools.athena.rest.feign.core.clients.EnvironmentClient;
import org.catools.athena.rest.feign.core.clients.ProjectClient;
import org.catools.athena.rest.feign.core.clients.UserClient;
import org.catools.athena.rest.feign.pipeline.clients.ExecutionClient;
import org.catools.athena.rest.feign.pipeline.clients.ExecutionStatusClient;
import org.catools.athena.rest.feign.pipeline.clients.PipelineClient;
import org.catools.athena.rest.feign.pipeline.clients.ScenarioExecutionClient;

import java.util.Optional;

import static org.catools.athena.rest.feign.client.FeignClient.getClient;

@UtilityClass
public class PipelineUtils {
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

  public static Optional<PipelineDto> getPipeline(final String pipelineName, final String pipelineNumber, final String environmentCode) {
    try {
      return Optional.ofNullable(getClient(PipelineClient.class).getPipeline(
          pipelineName,
          pipelineNumber,
          environmentCode
      ));
    } catch (FeignException.NotFound ex) {
      return Optional.empty();
    }
  }

  public static ProjectClient getProjectClient() {
    return getClient(ProjectClient.class);
  }

  public static EnvironmentClient getEnvironmentClient() {
    return getClient(EnvironmentClient.class);
  }

  public static UserClient getUserClient() {
    return getClient(UserClient.class);
  }

  public static ExecutionStatusClient getExecutionStatusClient() {
    return getClient(ExecutionStatusClient.class);
  }

  public static PipelineClient getPipelineClient() {
    return getClient(PipelineClient.class);
  }

  public static ExecutionClient getExecutionClient() {
    return getClient(ExecutionClient.class);
  }

  public static ScenarioExecutionClient getScenarioExecutionClient() {
    return getClient(ScenarioExecutionClient.class);
  }
}
