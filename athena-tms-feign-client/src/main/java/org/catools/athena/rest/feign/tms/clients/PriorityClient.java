package org.catools.athena.rest.feign.tms.clients;

import feign.*;
import org.catools.athena.tms.model.PriorityDto;

interface PriorityClient {

  @RequestLine("GET /tms/priority?code={code}")
  PriorityDto getByCode(
      @Param("code")
      String code);

  @RequestLine("POST /tms/priority")
  @Headers("Content-Type: application/json")
  void saveOrUpdate(PriorityDto priority);

}
