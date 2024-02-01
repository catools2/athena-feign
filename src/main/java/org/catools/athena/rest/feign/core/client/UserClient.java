package org.catools.athena.rest.feign.core.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.catools.athena.core.model.UserDto;

import java.util.Set;

public interface UserClient {
  @RequestLine("GET /user?keyword={keyword}")
  UserDto search(@Param("keyword") String keyword);

  @RequestLine("POST /user")
  @Headers("Content-Type: application/json")
  Response save(UserDto project);
}