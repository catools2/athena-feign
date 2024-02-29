package org.catools.athena.rest.feign.core.client;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.core.model.*;
import org.catools.athena.rest.feign.core.configs.CoreConfigs;

import java.util.Optional;

import static org.catools.athena.rest.feign.common.utils.FeignUtils.getClient;

@Slf4j
@UtilityClass
@SuppressWarnings("unused")
public class CoreClient {
  private static final ProjectClient PROJECT_CLIENT = getClient(ProjectClient.class, CoreConfigs.getAthenaHost());
  private static final EnvironmentClient ENVIRONMENT_CLIENT = getClient(EnvironmentClient.class, CoreConfigs.getAthenaHost());
  private static final VersionClient VERSION_CLIENT = getClient(VersionClient.class, CoreConfigs.getAthenaHost());
  private static final UserClient USER_CLIENT = getClient(UserClient.class, CoreConfigs.getAthenaHost());

  public static ProjectDto getProject(ProjectDto project) {
    return Optional.ofNullable(PROJECT_CLIENT.getByCode(project.getCode())).orElseGet(() -> {
      PROJECT_CLIENT.saveOrUpdate(project);
      return PROJECT_CLIENT.getByCode(project.getCode());
    });
  }

  public static EnvironmentDto getEnvironment(EnvironmentDto environment) {
    return Optional.ofNullable(ENVIRONMENT_CLIENT.getByCode(environment.getCode())).orElseGet(() -> {
      ENVIRONMENT_CLIENT.saveOrUpdate(environment);
      return ENVIRONMENT_CLIENT.getByCode(environment.getCode());
    });
  }

  public static VersionDto getVersion(VersionDto version) {
    return Optional.ofNullable(VERSION_CLIENT.getByCode(version.getCode())).orElseGet(() -> {
      VERSION_CLIENT.saveOrUpdate(version);
      return VERSION_CLIENT.getByCode(version.getCode());
    });
  }

  public static UserDto getUser(UserDto user) {
    return Optional.ofNullable(searchUser(user)).orElseGet(() -> {
      USER_CLIENT.saveOrUpdate(user);
      return searchUser(user);
    });
  }

  private static UserDto searchUser(UserDto user) {
    UserDto userDto;
    for (UserAliasDto alias : user.getAliases()) {
      userDto = USER_CLIENT.search(alias.getAlias());
      if (userDto != null) {
        return userDto;
      }
    }
    return USER_CLIENT.search(user.getUsername());
  }
}
