package org.catools.athena.rest.feign.core.client;

import feign.*;
import org.catools.athena.core.model.VersionDto;

interface VersionClient {

  @RequestLine("GET /version?code={code}")
  VersionDto getByCode(
      @Param("code")
      String code);

  @RequestLine("POST /version")
  @Headers("Content-Type: application/json")
  void saveOrUpdate(VersionDto version);

}
