package org.catools.athena.rest.feign.pipeline.helpers;

import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.core.model.EnvironmentDto;
import org.catools.athena.core.model.MetadataDto;
import org.catools.athena.core.model.ProjectDto;
import org.catools.athena.core.model.UserDto;
import org.catools.athena.pipeline.model.PipelineDto;
import org.catools.athena.pipeline.model.PipelineExecutionDto;
import org.catools.athena.pipeline.model.PipelineExecutionStatusDto;
import org.catools.athena.pipeline.model.PipelineScenarioExecutionDto;
import org.catools.athena.rest.feign.common.configs.CoreConfigs;
import org.catools.athena.rest.feign.common.configs.PipelineConfigs;
import org.catools.athena.rest.feign.core.client.UserClient;
import org.catools.athena.rest.feign.pipeline.clients.ExecutionStatusClient;
import org.catools.athena.rest.feign.pipeline.utils.PipelineUtils;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Slf4j
public class PipelineHelper {
  public static final String PROJECT_CODE = CoreConfigs.getProjectCode();
  public static final String PROJECT_NAME = CoreConfigs.getProjectName();
  public static final String ENVIRONMENT_CODE = CoreConfigs.getEnvironmentCode();
  public static final String ENVIRONMENT_NAME = CoreConfigs.getEnvironmentName();
  public static final String EXECUTOR_NAME = PipelineConfigs.getExecutorName();
  public static final String PIPELINE_NAME = PipelineConfigs.getPipelineName();
  public static final String PIPELINE_DESCRIPTION = PipelineConfigs.getPipelineDescription();
  public static final String PIPELINE_NUMBER = PipelineConfigs.getPipelineNumber();

  public static PipelineDto getPipeline() {
    return Optional.ofNullable(PipelineUtils.getPipelineClient().getPipeline(PIPELINE_NAME, PIPELINE_NUMBER, ENVIRONMENT_CODE))
        .orElse(buildPipeline());
  }

  public static PipelineDto finishPipeline(PipelineDto pipeline) {
    return PipelineUtils.getPipelineClient().updatePipelineEndDate(pipeline.getId(), Instant.now());
  }

  public static Long addScenarioExecution(PipelineDto pipeline,
                                          String feature,
                                          String scenario,
                                          String parameters,
                                          String status,
                                          Instant startTime,
                                          Instant beforeScenarioStartTime,
                                          Instant beforeScenarioEndTime,
                                          List<MetadataDto> metadata) {
    addUserIfNotExists();
    addStatusIfNotExists(status);

    PipelineScenarioExecutionDto execution = new PipelineScenarioExecutionDto();

    Set<MetadataDto> pipelineExecutionMetadata = PipelineConfigs.getPipelineExecutionMetadata();
    if (metadata != null && !metadata.isEmpty()) {
      pipelineExecutionMetadata.addAll(metadata);
    }

    execution
        .setFeature(feature)
        .setScenario(scenario)
        .setParameters(parameters)
        .setPipelineId(pipeline.getId())
        .setStatus(status)
        .setExecutor(EXECUTOR_NAME)
        .setStartTime(startTime)
        .setEndTime(Instant.now())
        .setBeforeScenarioStartTime(beforeScenarioStartTime)
        .setBeforeScenarioEndTime(beforeScenarioEndTime)
        .setMetadata(pipelineExecutionMetadata);

    Response response = PipelineUtils.getScenarioExecutionClient().saveScenarioExecution(execution);
    return getIdFromLocation(response);
  }

  public static Long addExecution(PipelineDto pipeline,
                                  String packageName,
                                  String className,
                                  String methodName,
                                  String parameters,
                                  String status,
                                  Instant startTime,
                                  Instant testStartTime,
                                  Instant testEndTime,
                                  Instant beforeClassStartTime,
                                  Instant beforeClassEndTime,
                                  Instant beforeMethodStartTime,
                                  Instant beforeMethodEndTime,
                                  List<MetadataDto> metadata) {
    addUserIfNotExists();
    addStatusIfNotExists(status);

    Set<MetadataDto> executionMetadata = PipelineConfigs.getPipelineExecutionMetadata();
    if (metadata != null && !metadata.isEmpty()) {
      executionMetadata.addAll(metadata);
    }

    PipelineExecutionDto executionDto = new PipelineExecutionDto();
    executionDto.setPackageName(packageName);
    executionDto.setClassName(className);
    executionDto.setMethodName(methodName);
    executionDto.setParameters(parameters);
    executionDto.setStartTime(startTime);
    executionDto.setEndTime(Instant.now());
    executionDto.setTestStartTime(testStartTime);
    executionDto.setTestEndTime(testEndTime);
    executionDto.setBeforeClassStartTime(beforeClassStartTime);
    executionDto.setBeforeClassEndTime(beforeClassEndTime);
    executionDto.setBeforeMethodStartTime(beforeMethodStartTime);
    executionDto.setBeforeMethodEndTime(beforeMethodEndTime);
    executionDto.setStatus(status);
    executionDto.setExecutor(EXECUTOR_NAME);
    executionDto.setPipelineId(pipeline.getId());
    executionDto.setMetadata(executionMetadata);
    Response response = PipelineUtils.getExecutionClient().saveExecution(executionDto);
    return getIdFromLocation(response);
  }

  private static PipelineDto buildPipeline() {
    if (PipelineUtils.getProjectClient().getByCode(PROJECT_CODE) == null) {
      PipelineUtils.getProjectClient().save(
          new ProjectDto()
              .setName(PROJECT_NAME)
              .setCode(PROJECT_CODE)
      );
    }

    if (PipelineUtils.getEnvironmentClient().getEnvironment(ENVIRONMENT_CODE) == null) {
      PipelineUtils.getEnvironmentClient().save(
          new EnvironmentDto()
              .setName(ENVIRONMENT_NAME)
              .setCode(ENVIRONMENT_CODE)
              .setProject(PROJECT_CODE)
      );
    }

    final PipelineDto pipeline = new PipelineDto()
        .setName(PIPELINE_NAME)
        .setDescription(PIPELINE_DESCRIPTION)
        .setNumber(PIPELINE_NUMBER)
        .setStartDate(Instant.now())
        .setEnvironmentCode(ENVIRONMENT_CODE)
        .setMetadata(PipelineConfigs.getPipelineMetadata());

    if (PipelineUtils.getPipeline(pipeline.getName(), pipeline.getNumber(), pipeline.getEnvironmentCode()).isEmpty()) {
      Set<MetadataDto> metadata = new HashSet<>();
      for (MetadataDto md : pipeline.getMetadata()) {
        metadata.add(new MetadataDto().setName(md.getName()).setValue(md.getValue()));
      }

      Response response = PipelineUtils.getPipelineClient().savePipeline(
          new PipelineDto()
              .setName(pipeline.getName())
              .setNumber(pipeline.getNumber())
              .setEnvironmentCode(ENVIRONMENT_CODE)
              .setDescription(pipeline.getDescription())
              .setStartDate(pipeline.getStartDate())
              .setEndDate(pipeline.getEndDate())
              .setMetadata(metadata)
      );
      pipeline.setId(getIdFromLocation(response));
    }

    return pipeline;
  }

  private synchronized static void addUserIfNotExists() {
    UserClient userClient = PipelineUtils.getUserClient();
    if (userClient.search(EXECUTOR_NAME) == null) {
      userClient.save(new UserDto().setUsername(EXECUTOR_NAME));
    }
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
    if (location.isEmpty()) return null;
    String[] pathParts = location.get().split("/");
    return Long.valueOf(pathParts[pathParts.length - 1]);
  }
}
