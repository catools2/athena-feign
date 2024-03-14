package org.catools.athena.rest.feign.tms.clients;

import feign.*;
import org.catools.athena.tms.model.TestCycleDto;

interface TestCycleClient {

  @RequestLine("GET /tms/cycle/{code}/hash")
  Integer getUniqueHashByCode(
      @Param("code")
      String code);

  @RequestLine("GET /tms/cycle?keyword={keyword}")
  TestCycleDto findByCode(
      @Param("keyword")
      String keyword);

  @RequestLine("GET /tms/cycles?name={name}&versionCode={versionCode}")
  TestCycleDto findLastByPattern(
      @Param("name")
      String name,
      @Param("versionCode")
      String versionCode);

  @RequestLine("POST /tms/cycle")
  @Headers("Content-Type: application/json")
  void saveOrUpdate(TestCycleDto cycle);

}
