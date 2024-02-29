package org.catools.athena.rest.feign.apispec.client;

import feign.Headers;
import feign.RequestLine;
import org.catools.athena.apispec.model.ApiSpecDto;

public interface ApiSpecClient {

  @RequestLine("POST /oai/spec")
  @Headers("Content-Type: application/json")
  void saveOrUpdate(ApiSpecDto apiSpec);

}
