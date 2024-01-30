package org.catools.athena.rest.feign.git.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.catools.athena.git.model.CommitDto;

import java.util.Set;


public interface CommitClient {
  @RequestLine("GET /commit?keyword={keyword}")
  CommitDto search(@Param("keyword") String keyword);

  @RequestLine("GET /commit/{id}")
  CommitDto getById(@Param("id") String id);

  @RequestLine("GET /commits")
  Set<CommitDto> getAll();

  @RequestLine("POST /commit")
  @Headers("Content-Type: application/json")
  Response save(CommitDto project);
}
