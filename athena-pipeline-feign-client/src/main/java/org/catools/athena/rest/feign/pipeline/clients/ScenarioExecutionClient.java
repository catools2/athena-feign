package org.catools.athena.rest.feign.pipeline.clients;

import feign.*;
import org.catools.athena.pipeline.model.PipelineScenarioExecutionDto;

public interface ScenarioExecutionClient {
  @RequestLine("POST /scenario")
  @Headers("Content-Type: application/json")
  Response saveScenarioExecution(PipelineScenarioExecutionDto execution);

}
