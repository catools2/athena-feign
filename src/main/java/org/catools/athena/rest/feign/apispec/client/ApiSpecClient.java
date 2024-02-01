package org.catools.athena.rest.feign.apispec.client;

import feign.Headers;
import feign.RequestLine;
import feign.Response;
import org.catools.athena.apispec.model.ApiSpecDto;

public interface ApiSpecClient {

  @RequestLine("POST /apiSpec")
  @Headers("Content-Type: application/json")
  Response save(ApiSpecDto apiSpec);

}
