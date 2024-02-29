package org.catools.athena.rest.feign.core.client;

import feign.*;
import org.catools.athena.core.model.ProjectDto;

interface ProjectClient {
  @RequestLine("GET /project?code={code}")
  ProjectDto getByCode(
      @Param("code")
      String code);

  @RequestLine("POST /project")
  @Headers("Content-Type: application/json")
  void saveOrUpdate(ProjectDto project);
}
