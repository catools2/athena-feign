package org.catools.athena.rest.feign.pipeline.helpers;

import org.catools.athena.core.model.MetadataDto;
import org.catools.athena.pipeline.model.PipelineDto;
import org.catools.athena.pipeline.model.PipelineExecutionDto;
import org.catools.athena.pipeline.model.PipelineScenarioExecutionDto;
import org.catools.athena.rest.feign.pipeline.utils.PipelineUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PipelineHelperTest {
  @Test
  @Order(1)
  void GivenPipelineNotExists_GetPipelineShallReturnNewPipeline() {
    PipelineDto pipeline = PipelineHelper.getPipeline();
    assertThat(pipeline).isNotNull();
    assertThat(pipeline.getId()).isEqualTo(1L);
    assertThat(pipeline.getEndDate()).isNull();
  }

  @Test
  @Order(2)
  void GivenPipelineExists_GetPipelineShallReturnExistingPipeline() {
    PipelineDto pipeline = PipelineHelper.getPipeline();
    assertThat(pipeline).isNotNull();
    assertThat(pipeline.getId()).isEqualTo(1L);
  }

  @Test
  @Order(4)
  void addScenarioExecution() {
    PipelineDto pipeline = PipelineHelper.getPipeline();
    String feature = "Test Feature";
    String scenario = "Test Scenario";
    String parameters = "P1,P2,P3";
    String status = "Passed";
    ZonedDateTime dateTime = LocalDateTime.now().minusHours(1L).atZone(ZoneId.of("America/New_York"));
    Instant startTime = dateTime.toInstant();
    Instant beforeScenarioStartTime = dateTime.plusMinutes(1L).toInstant();
    Instant beforeScenarioEndTime = dateTime.plusMinutes(3L).toInstant();
    List<MetadataDto> metadata = List.of(new MetadataDto().setName("E3").setValue("V3"));

    Long id = PipelineHelper.addScenarioExecution(pipeline,
        feature,
        scenario,
        parameters,
        status,
        startTime,
        beforeScenarioStartTime,
        beforeScenarioEndTime,
        metadata);

    assertThat(id).isNotNull();

    PipelineScenarioExecutionDto scenarioExecutionDto = PipelineUtils.getScenarioExecutionClient().getScenarioExecution(id);

    assertThat(scenarioExecutionDto).isNotNull();
    assertThat(scenarioExecutionDto.getId()).isNotNull();
    assertThat(scenarioExecutionDto.getFeature()).isEqualTo(feature);
    assertThat(scenarioExecutionDto.getScenario()).isEqualTo(scenario);
    assertThat(scenarioExecutionDto.getParameters()).isEqualTo(parameters);
    assertThat(scenarioExecutionDto.getStatus()).isEqualTo(status);
    assertThat(scenarioExecutionDto.getStartTime().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(startTime.truncatedTo(ChronoUnit.MILLIS));
    assertThat(scenarioExecutionDto.getBeforeScenarioStartTime().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(beforeScenarioStartTime.truncatedTo(ChronoUnit.MILLIS));
    assertThat(scenarioExecutionDto.getBeforeScenarioEndTime().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(beforeScenarioEndTime.truncatedTo(ChronoUnit.MILLIS));
    assertThat(scenarioExecutionDto.getEndTime()).isNotNull();
    assertThat(getMetadataValue(scenarioExecutionDto, "E1")).isEqualTo("V1");
    assertThat(getMetadataValue(scenarioExecutionDto, "E2")).isEqualTo("V2");
    assertThat(getMetadataValue(scenarioExecutionDto, "E3")).isEqualTo("V3");
  }

  @Test
  @Order(3)
  void addExecution() {
    PipelineDto pipeline = PipelineHelper.getPipeline();

    String packageName = "PackageName";
    String className = "ClassName";
    String methodName = "MethodName";
    String parameters = "P1,P2,P3";
    String status = "Passed";
    ZonedDateTime dateTime = LocalDateTime.now(ZoneOffset.UTC).minusHours(1L).atZone(ZoneOffset.UTC);
    Instant startTime = dateTime.toInstant();
    Instant testStartTime = dateTime.plusMinutes(1L).toInstant();
    Instant testEndTime = dateTime.plusMinutes(10L).toInstant();
    Instant beforeClassStartTime = dateTime.plusMinutes(3L).toInstant();
    Instant beforeClassEndTime = dateTime.plusMinutes(4L).toInstant();
    Instant beforeMethodStartTime = dateTime.plusMinutes(5L).toInstant();
    Instant beforeMethodEndTime = dateTime.plusMinutes(6L).toInstant();
    List<MetadataDto> metadata = List.of(new MetadataDto().setName("E3").setValue("V3"));

    Long id = PipelineHelper.addExecution(pipeline,
        packageName,
        className,
        methodName,
        parameters,
        status,
        startTime,
        testStartTime,
        testEndTime,
        beforeClassStartTime,
        beforeClassEndTime,
        beforeMethodStartTime,
        beforeMethodEndTime,
        metadata);

    assertThat(id).isNotNull();

    PipelineExecutionDto executionDto = PipelineUtils.getExecutionClient().getExecution(id);

    assertThat(executionDto).isNotNull();
    assertThat(executionDto.getId()).isNotNull();
    assertThat(executionDto.getPackageName()).isEqualTo(packageName);
    assertThat(executionDto.getClassName()).isEqualTo(className);
    assertThat(executionDto.getMethodName()).isEqualTo(methodName);
    assertThat(executionDto.getParameters()).isEqualTo(parameters);
    assertThat(executionDto.getStatus()).isEqualTo(status);
    assertThat(executionDto.getStartTime().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(startTime.truncatedTo(ChronoUnit.MILLIS));
    assertThat(executionDto.getTestStartTime().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(testStartTime.truncatedTo(ChronoUnit.MILLIS));
    assertThat(executionDto.getTestEndTime().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(testEndTime.truncatedTo(ChronoUnit.MILLIS));
    assertThat(executionDto.getBeforeClassStartTime().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(beforeClassStartTime.truncatedTo(ChronoUnit.MILLIS));
    assertThat(executionDto.getBeforeClassEndTime().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(beforeClassEndTime.truncatedTo(ChronoUnit.MILLIS));
    assertThat(executionDto.getBeforeMethodStartTime().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(beforeMethodStartTime.truncatedTo(ChronoUnit.MILLIS));
    assertThat(executionDto.getBeforeMethodEndTime().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(beforeMethodEndTime.truncatedTo(ChronoUnit.MILLIS));
    assertThat(executionDto.getEndTime()).isNotNull();
    assertThat(getMetadataValue(executionDto, "E1")).isEqualTo("V1");
    assertThat(getMetadataValue(executionDto, "E2")).isEqualTo("V2");
    assertThat(getMetadataValue(executionDto, "E3")).isEqualTo("V3");
  }

  private static String getMetadataValue(PipelineScenarioExecutionDto execution, String name) {
    return execution.getMetadata().stream().filter(f -> f.getName().equals(name)).findFirst().map(MetadataDto::getValue).orElse(null);
  }

  private static String getMetadataValue(PipelineExecutionDto execution, String name) {
    return execution.getMetadata().stream().filter(f -> f.getName().equals(name)).findFirst().map(MetadataDto::getValue).orElse(null);
  }

  @Test
  @Order(10)
  void finishPipeline() {
    PipelineDto pipeline = PipelineHelper.getPipeline();
    PipelineDto updatedPipeline = PipelineHelper.finishPipeline(pipeline);
    assertThat(updatedPipeline).isNotNull();
    assertThat(updatedPipeline.getEndDate()).isNotNull();
  }
}