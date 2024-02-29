package org.catools.athena.rest.feign.apispec.client;

import feign.*;
import org.catools.athena.git.model.GitRepositoryDto;


public interface RepositoryClient {
  @RequestLine("GET /repo?keyword={keyword}")
  GitRepositoryDto search(
      @Param("keyword")
      String keyword);

  @RequestLine("POST /repo")
  @Headers("Content-Type: application/json")
  Response save(GitRepositoryDto repository);
}
