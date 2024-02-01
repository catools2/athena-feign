package org.catools.athena.rest.feign.apispec.helpers;

import feign.Response;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.apispec.model.ApiPathDto;
import org.catools.athena.apispec.model.ApiSpecDto;
import org.catools.athena.apispec.utils.ApiSpecDtoUtils;
import org.catools.athena.core.model.ProjectDto;
import org.catools.athena.rest.feign.apispec.client.ApiPathClient;
import org.catools.athena.rest.feign.apispec.client.ApiSpecClient;
import org.catools.athena.rest.feign.common.configs.CoreConfigs;
import org.catools.athena.rest.feign.core.client.ProjectClient;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static org.catools.athena.rest.feign.common.utils.FeignUtils.getClient;
import static org.catools.athena.rest.feign.common.utils.FeignUtils.getEntityId;

@Slf4j
@UtilityClass
@SuppressWarnings("unused")
public class ApiSpecLoader {
  public static final String PROJECT_CODE = CoreConfigs.getProjectCode();
  public static final String PROJECT_NAME = CoreConfigs.getProjectName();
  public static final ProjectClient PROJECT_CLIENT = getClient(ProjectClient.class);
  public static final ApiSpecClient API_SPEC_CLIENT = getClient(ApiSpecClient.class);
  public static final ApiPathClient API_PATH_CLIENT = getClient(ApiPathClient.class);

  public static ProjectDto getProject() {
    ProjectDto project = PROJECT_CLIENT.getByCode(PROJECT_CODE);
    if (project != null) {
      return project;
    }

    PROJECT_CLIENT.save(new ProjectDto().setName(PROJECT_NAME).setCode(PROJECT_CODE));
    return PROJECT_CLIENT.getByCode(PROJECT_CODE);
  }

  public static void saveOpenApi(String specName, URL url) throws IOException {
    OpenAPI openAPI = Json.mapper().readValue(url, OpenAPI.class);
    getEntityId(saveApiSpec(openAPI, specName)).ifPresent(specId -> saveApiPaths(openAPI, specId));
  }

  public static Response saveApiSpec(OpenAPI openAPI, String specName) {
    ApiSpecDto apiSpec = ApiSpecDtoUtils.getApiSpec(openAPI, specName, getProject().getCode());
    return API_SPEC_CLIENT.save(apiSpec);
  }

  public static Set<Response> saveApiPaths(OpenAPI openAPI, Long apiSpecId) {
    Set<Response> responses = new HashSet<>();
    Set<ApiPathDto> apiPaths = ApiSpecDtoUtils.getApiPaths(openAPI, apiSpecId);

    for (ApiPathDto apiPath : apiPaths) {
      responses.add(saveApiPath(apiPath));
    }

    return responses;
  }

  public static Response saveApiPath(ApiPathDto apiPath) {
    return API_PATH_CLIENT.save(apiPath);
  }

}
