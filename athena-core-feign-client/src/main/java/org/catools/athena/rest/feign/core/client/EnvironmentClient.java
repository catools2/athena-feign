package org.catools.athena.rest.feign.core.client;

import feign.*;
import org.catools.athena.core.model.EnvironmentDto;

interface EnvironmentClient {
  @RequestLine("GET /environment?code={code}")
  EnvironmentDto getByCode(
      @Param("code")
      String code);

  @RequestLine("POST /environment")
  @Headers("Content-Type: application/json")
  void saveOrUpdate(EnvironmentDto environment);
}
