package org.catools.athena.rest.feign.apispec.client;

import feign.Headers;
import feign.RequestLine;
import feign.Response;
import org.catools.athena.apispec.model.ApiPathDto;

public interface ApiPathClient {

  @RequestLine("POST /apiPath")
  @Headers("Content-Type: application/json")
  Response save(ApiPathDto apiSpec);

}
