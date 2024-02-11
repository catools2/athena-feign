package org.catools.athena.rest.feign.core.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.catools.athena.core.model.ProjectDto;

public interface ProjectClient {
  @RequestLine("GET /project?projectCode={projectCode}")
  ProjectDto getByCode(@Param("projectCode") String projectCode);

  @RequestLine("POST /project")
  @Headers("Content-Type: application/json")
  Response save(ProjectDto project);
}
