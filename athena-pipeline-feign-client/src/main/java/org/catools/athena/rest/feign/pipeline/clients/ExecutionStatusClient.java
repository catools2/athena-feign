package org.catools.athena.rest.feign.pipeline.clients;

import feign.*;
import org.catools.athena.pipeline.model.PipelineExecutionStatusDto;

public interface ExecutionStatusClient {

  @RequestLine("GET /execution_status?name={name}")
  PipelineExecutionStatusDto getExecutionStatus(
      @Param("name")
      String name);

  @RequestLine("POST /execution_status")
  @Headers("Content-Type: application/json")
  Response saveExecutionStatus(PipelineExecutionStatusDto pipelineExecutionStatusDto);
}
