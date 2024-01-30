package org.catools.athena.rest.feign.pipeline.clients;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.catools.athena.pipeline.model.PipelineExecutionDto;
import org.catools.athena.pipeline.model.PipelineScenarioExecutionDto;

public interface ExecutionClient {

    @RequestLine("POST /execution")
    @Headers("Content-Type: application/json")
    Response saveExecution(PipelineExecutionDto project);

    @RequestLine("GET /execution/{id}")
    PipelineExecutionDto getExecution(@Param("id") Long id);
}
