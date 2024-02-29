package org.catools.athena.rest.feign.tms.clients;

import feign.*;
import org.catools.athena.tms.model.TestCycleDto;

interface TestCycleClient {

  @RequestLine("GET /tms/cycle?code={code}")
  TestCycleDto getByCode(
      @Param("code")
      String code);

  @RequestLine("POST /tms/cycle")
  @Headers("Content-Type: application/json")
  void saveOrUpdate(TestCycleDto cycle);

}
