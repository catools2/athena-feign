package org.catools.athena.rest.feign.core.clients;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.catools.athena.core.model.EnvironmentDto;

import java.util.Set;

public interface EnvironmentClient {
    @RequestLine("GET /environment?envCode={envCode}")
    EnvironmentDto getEnvironment(@Param("envCode") String envCode);

    @RequestLine("GET /environments")
    Set<EnvironmentDto> getEnvironments();

    @RequestLine("POST /environment")
    @Headers("Content-Type: application/json")
    EnvironmentDto saveEnvironment(EnvironmentDto project);
}
