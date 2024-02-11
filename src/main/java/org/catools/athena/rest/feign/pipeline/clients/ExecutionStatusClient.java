package org.catools.athena.rest.feign.pipeline.clients;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.catools.athena.pipeline.model.PipelineExecutionStatusDto;

import java.util.Set;

public interface ExecutionStatusClient {
  @RequestLine("GET /execution_statuses")
  Set<PipelineExecutionStatusDto> getExecutionStatuses();

  @RequestLine("GET /execution_status?name={name}")
  PipelineExecutionStatusDto getExecutionStatus(@Param("name") String username);

  @RequestLine("POST /execution_status")
  @Headers("Content-Type: application/json")
  Response saveExecutionStatus(PipelineExecutionStatusDto pipelineExecutionStatusDto);
}
