package org.catools.athena.rest.feign.pipeline.clients;

import feign.*;
import org.catools.athena.pipeline.model.PipelineDto;

import java.time.Instant;

public interface PipelineClient {
  @RequestLine("GET /pipeline?name={name}&number={number}&envCode={envCode}")
  PipelineDto getPipeline(
      @Param("name")
      String pipelineName,
      @Param("number")
      String pipelineNumber,
      @Param("envCode")
      String environmentCode);

  @RequestLine("PATCH /pipeline?pipelineId={pipelineId}&endDate={date}")
  PipelineDto updatePipelineEndDate(
      @Param("pipelineId")
      Long pipelineId,
      @Param("endDate")
      Instant instant);

  @RequestLine("POST /pipeline")
  @Headers("Content-Type: application/json")
  Response savePipeline(PipelineDto project);
}
