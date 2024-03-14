package org.catools.athena.rest.feign.core.client;

import feign.*;
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
