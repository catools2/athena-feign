package org.catools.athena.rest.feign.pipeline.helpers;

import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.core.model.*;
import org.catools.athena.pipeline.model.*;
import org.catools.athena.rest.feign.core.client.CoreClient;
import org.catools.athena.rest.feign.pipeline.clients.ExecutionStatusClient;
import org.catools.athena.rest.feign.pipeline.utils.PipelineUtils;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.*;


@Slf4j
public class PipelineHelper {

  public static PipelineDto getPipeline(String host,
                                        String projectCode,
                                        String environmentCode,
                                        String environmentName,
                                        String pipelineName,
                                        String pipelineNumber,
                                        String pipelineDescription,
                                        Set<MetadataDto> metadataDto) {
    return Optional.ofNullable(PipelineUtils.getPipelineClient().getPipeline(pipelineName, pipelineNumber, environmentCode))
                   .orElse(buildPipeline(host,
                       projectCode,
                       environmentCode,
                       environmentName,
                       pipelineName,
                       pipelineNumber,
                       pipelineDescription,
                       metadataDto));
  }

  public static PipelineDto finishPipeline(PipelineDto pipeline) {
    return PipelineUtils.getPipelineClient().updatePipelineEndDate(pipeline.getId(), Instant.now());
  }

  public static Long addScenarioExecution(PipelineScenarioExecutionDto execution) {
    addUserIfNotExists(execution.getExecutor());
    addStatusIfNotExists(execution.getStatus());

    Response response = PipelineUtils.getScenarioExecutionClient().saveScenarioExecution(execution);
    return getIdFromLocation(response);
  }

  public static Long addExecution(PipelineExecutionDto executionDto) {
    addUserIfNotExists(executionDto.getExecutor());
    addStatusIfNotExists(executionDto.getStatus());

    Response response = PipelineUtils.getExecutionClient().saveExecution(executionDto);
    return getIdFromLocation(response);
  }

  public static PipelineDto buildPipeline(String host,
                                          String projectCode,
                                          String environmentCode,
                                          String environmentName,
                                          String pipelineName,
                                          String pipelineNumber,
                                          String pipelineDescription,
                                          Set<MetadataDto> metadataDto) {
    CoreClient.getEnvironment(new EnvironmentDto(environmentCode, environmentName, projectCode));

    final PipelineDto pipeline = new PipelineDto().setName(pipelineName)
                                                  .setDescription(pipelineDescription)
                                                  .setNumber(pipelineNumber)
                                                  .setStartDate(Instant.now())
                                                  .setEnvironment(environmentCode)
                                                  .setMetadata(metadataDto);

    if (PipelineUtils.getPipeline(pipeline.getName(), pipeline.getNumber(), pipeline.getEnvironment()).isEmpty()) {
      Set<MetadataDto> metadata = new HashSet<>();
      for (MetadataDto md : pipeline.getMetadata()) {
        metadata.add(new MetadataDto().setName(md.getName()).setValue(md.getValue()));
      }

      Response response = PipelineUtils.getPipelineClient()
                                       .savePipeline(new PipelineDto().setName(pipeline.getName())
                                                                      .setNumber(pipeline.getNumber())
                                                                      .setEnvironment(environmentCode)
                                                                      .setDescription(pipeline.getDescription())
                                                                      .setStartDate(pipeline.getStartDate())
                                                                      .setEndDate(pipeline.getEndDate())
                                                                      .setMetadata(metadata));
      pipeline.setId(getIdFromLocation(response));
    }

    return pipeline;
  }

  private synchronized static void addUserIfNotExists(String username) {
    CoreClient.getUser(new UserDto().setUsername(username));
  }

  private synchronized static void addStatusIfNotExists(String status) {
    ExecutionStatusClient executionStatusClient = PipelineUtils.getExecutionStatusClient();
    if (executionStatusClient.getExecutionStatus(status) == null) {
      executionStatusClient.saveExecutionStatus(new PipelineExecutionStatusDto().setName(status));
    }
  }

  @Nullable
  private static Long getIdFromLocation(Response response) {
    Optional<String> location = response.headers().get("location").stream().findFirst();
    if (location.isEmpty()) {
      return null;
    }
    String[] pathParts = location.get().split("/");
    return Long.valueOf(pathParts[pathParts.length - 1]);
  }
}
