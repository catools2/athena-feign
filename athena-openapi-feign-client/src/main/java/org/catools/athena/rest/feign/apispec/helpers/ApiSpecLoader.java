package org.catools.athena.rest.feign.apispec.helpers;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.apispec.model.ApiSpecDto;
import org.catools.athena.rest.feign.apispec.client.ApiSpecClient;
import org.catools.athena.rest.feign.apispec.utils.ApiSpecUtils;
import org.catools.athena.rest.feign.core.configs.CoreConfigs;

import java.io.IOException;
import java.net.URL;

import static org.catools.athena.rest.feign.common.utils.FeignUtils.getClient;

@Slf4j
@UtilityClass
@SuppressWarnings("unused")
public class ApiSpecLoader {
  public static final ApiSpecClient API_SPEC_CLIENT = getClient(ApiSpecClient.class, CoreConfigs.getAthenaHost());

  public static void saveOpenApi(String specName, URL url, String projectCode) throws IOException {
    OpenAPI openAPI = Json.mapper().readValue(url, OpenAPI.class);
    saveApiSpec(openAPI, specName, projectCode);
  }

  public static void saveApiSpec(OpenAPI openAPI, String specName, String projectCode) {
    ApiSpecDto apiSpec = ApiSpecUtils.getApiSpec(openAPI, specName, projectCode);
    API_SPEC_CLIENT.saveOrUpdate(apiSpec);
  }
}
