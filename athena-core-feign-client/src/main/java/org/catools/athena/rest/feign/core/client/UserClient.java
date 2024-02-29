package org.catools.athena.rest.feign.core.client;

import feign.*;
import org.catools.athena.core.model.UserDto;

interface UserClient {
  @RequestLine("GET /user?keyword={keyword}")
  UserDto search(
      @Param("keyword")
      String keyword);

  @RequestLine("POST /user")
  @Headers("Content-Type: application/json")
  void saveOrUpdate(UserDto user);
}
