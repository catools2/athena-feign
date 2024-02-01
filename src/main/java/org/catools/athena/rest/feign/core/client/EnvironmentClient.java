package org.catools.athena.rest.feign.core.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.catools.athena.core.model.EnvironmentDto;

public interface EnvironmentClient {
  @RequestLine("GET /environment?envCode={envCode}")
  EnvironmentDto getEnvironment(@Param("envCode") String envCode);

  @RequestLine("POST /environment")
  @Headers("Content-Type: application/json")
  Response save(EnvironmentDto project);
}
