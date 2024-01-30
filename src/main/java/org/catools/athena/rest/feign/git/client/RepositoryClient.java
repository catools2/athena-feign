package org.catools.athena.rest.feign.git.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.catools.athena.git.model.GitRepositoryDto;

import java.util.Set;


public interface RepositoryClient {
  @RequestLine("GET /repo?keyword={keyword}")
  GitRepositoryDto search(@Param("keyword") String keyword);

  @RequestLine("GET /repo/{id}")
  GitRepositoryDto getById(@Param("id") String id);

  @RequestLine("GET /repos")
  Set<GitRepositoryDto> getAll();

  @RequestLine("POST /repo")
  @Headers("Content-Type: application/json")
  Response save(GitRepositoryDto project);
}
