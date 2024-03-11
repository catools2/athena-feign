package org.catools.athena.rest.feign.core.client;

import feign.*;
import org.catools.athena.core.model.ProjectDto;

interface ProjectClient {
  @RequestLine("GET /project?keyword={keyword}")
  ProjectDto search(
      @Param("keyword")
      String keyword);

  @RequestLine("POST /project")
  @Headers("Content-Type: application/json")
  void saveOrUpdate(ProjectDto project);
}
