package org.catools.athena.rest.feign.pipeline.clients;

import feign.Headers;
import feign.RequestLine;
import feign.Response;
import org.catools.athena.pipeline.model.PipelineExecutionDto;

public interface ExecutionClient {

  @RequestLine("POST /execution")
  @Headers("Content-Type: application/json")
  Response saveExecution(PipelineExecutionDto project);

}
