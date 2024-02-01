package org.catools.athena.rest.feign.git.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.catools.athena.core.model.MetadataDto;

import java.util.Set;

public interface CommitMetadataClient {
  @RequestLine("GET /commit_md?name={name}&value={value}")
  MetadataDto search(@Param("name") String name, @Param("value") String value);

  @RequestLine("GET /commit_mds")
  Set<MetadataDto> getAll();

  @RequestLine("POST /commit_md")
  @Headers("Content-Type: application/json")
  Response save(MetadataDto project);
}
