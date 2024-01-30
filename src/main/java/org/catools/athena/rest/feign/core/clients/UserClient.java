package org.catools.athena.rest.feign.core.clients;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.catools.athena.core.model.UserDto;

import java.util.Set;

public interface UserClient {
  @RequestLine("GET /user?keyword={keyword}")
  UserDto search(@Param("keyword") String keyword);

  @RequestLine("GET /users")
  Set<UserDto> getAll();

  @RequestLine("POST /user")
  @Headers("Content-Type: application/json")
  Response save(UserDto project);
}
