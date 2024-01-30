package org.catools.athena.rest.feign.git.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.catools.athena.git.model.TagDto;

import java.util.Set;


public interface TagClient {
  @RequestLine("GET /tag?keyword={keyword}")
  TagDto search(@Param("keyword") String keyword);

  @RequestLine("GET /tag/{id}")
  TagDto getById(@Param("id") String id);

  @RequestLine("GET /tags")
  Set<TagDto> getAll();

  @RequestLine("POST /tag")
  @Headers("Content-Type: application/json")
  Response save(TagDto project);
}
