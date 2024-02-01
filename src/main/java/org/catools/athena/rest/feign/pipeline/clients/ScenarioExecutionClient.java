package org.catools.athena.rest.feign.pipeline.clients;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.catools.athena.pipeline.model.PipelineScenarioExecutionDto;

public interface ScenarioExecutionClient {
  @RequestLine("POST /scenario")
  @Headers("Content-Type: application/json")
  Response saveScenarioExecution(PipelineScenarioExecutionDto execution);

  @RequestLine("GET /scenario/{id}")
  PipelineScenarioExecutionDto getScenarioExecution(@Param("id") Long id);
}
