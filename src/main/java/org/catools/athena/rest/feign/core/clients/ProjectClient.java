package org.catools.athena.rest.feign.core.clients;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.catools.athena.core.model.ProjectDto;

import java.util.Set;

public interface ProjectClient {
    @RequestLine("GET /project?projectCode={projectCode}")
    ProjectDto getProject(@Param("projectCode") String projectCode);

    @RequestLine("GET /projects")
    Set<ProjectDto> getProjects();

    @RequestLine("POST /project")
    @Headers("Content-Type: application/json")
    ProjectDto saveProject(ProjectDto project);
}
