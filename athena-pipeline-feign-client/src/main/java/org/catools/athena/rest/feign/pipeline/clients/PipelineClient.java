package org.catools.athena.rest.feign.pipeline.clients;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.catools.athena.pipeline.model.PipelineDto;

import java.time.Instant;

public interface PipelineClient {
  @RequestLine("GET /pipeline?name={name}&number={number}&versionCode={versionCode}&envCode={envCode}")
  PipelineDto getPipeline(
      @Param("name")
      String pipelineName,
      @Param("number")
      String pipelineNumber,
      @Param("versionCode")
      String versionCode,
      @Param("envCode")
      String environmentCode);

  @RequestLine("PATCH /pipeline?pipelineId={pipelineId}&date={date}")
  PipelineDto updatePipelineEndDate(
      @Param("pipelineId")
      Long pipelineId,
      @Param("date")
      Instant instant);

  @RequestLine("POST /pipeline")
  @Headers("Content-Type: application/json")
  Response savePipeline(PipelineDto project);
}
