package org.catools.athena.rest.feign.core.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.catools.athena.core.model.VersionDto;

interface VersionClient {

  @RequestLine("GET /version?keyword={keyword}")
  VersionDto search(
      @Param("keyword")
      String keyword);

  @RequestLine("POST /version")
  @Headers("Content-Type: application/json")
  void saveOrUpdate(VersionDto version);

}
