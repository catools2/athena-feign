package org.catools.athena.rest.feign.tms.clients;

import feign.*;
import org.catools.athena.tms.model.StatusDto;

interface StatusClient {

  @RequestLine("GET /tms/status?code={code}")
  StatusDto getByCode(
      @Param("code")
      String code);

  @RequestLine("POST /tms/status")
  @Headers("Content-Type: application/json")
  void saveOrUpdate(StatusDto status);

}
