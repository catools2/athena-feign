package org.catools.athena.rest.feign.apispec.client;

import feign.Headers;
import feign.RequestLine;
import feign.Response;
import org.catools.athena.git.model.CommitDto;


public interface CommitClient {
  @RequestLine("POST /commit")
  @Headers("Content-Type: application/json")
  Response save(CommitDto commit);
}
