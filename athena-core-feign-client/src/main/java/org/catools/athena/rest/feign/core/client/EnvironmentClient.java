package org.catools.athena.rest.feign.core.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.catools.athena.core.model.EnvironmentDto;

interface EnvironmentClient {
  @RequestLine("GET /environment?keyword={keyword}")
  EnvironmentDto search(
      @Param("keyword")
      String keyword);

  @RequestLine("POST /environment")
  @Headers("Content-Type: application/json")
  void saveOrUpdate(EnvironmentDto environment);
}
