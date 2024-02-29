package org.catools.athena.rest.feign.tms.cache;

import org.catools.athena.core.model.*;
import org.catools.athena.rest.feign.core.client.CoreClient;
import org.catools.athena.rest.feign.tms.clients.TmsClient;
import org.catools.athena.tms.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CacheManager {
  private static final Map<String, UserDto> USERS = new HashMap<>();
  private static final Map<String, ProjectDto> PROJECTS = new HashMap<>();
  private static final Map<String, VersionDto> VERSIONS = new HashMap<>();
  private static final Map<String, StatusDto> STATUSES = new HashMap<>();
  private static final Map<String, PriorityDto> PRIORITIES = new HashMap<>();
  private static final Map<String, ItemTypeDto> ITEM_TYPES = new HashMap<>();

  public static synchronized UserDto readUser(UserDto user) {
    return read(USERS, user.getUsername(), () -> {
      UserDto result = CoreClient.getUser(user);
      if (result != null) {
        return result;
      }
      return CoreClient.getUser(user);
    });
  }

  public static synchronized ProjectDto readProject(ProjectDto project) {
    return read(PROJECTS, project.getCode(), () -> CoreClient.getProject(project));
  }

  public static synchronized VersionDto readVersion(VersionDto version) {
    return read(VERSIONS, version.getCode(), () -> CoreClient.getVersion(version));
  }

  public static synchronized StatusDto readStatus(StatusDto status) {
    return read(STATUSES, status.getCode(), () -> TmsClient.getStatus(status));
  }

  public static synchronized PriorityDto readPriority(PriorityDto priority) {
    return read(PRIORITIES, priority.getCode(), () -> TmsClient.getPriority(priority));
  }

  public static synchronized ItemTypeDto readType(ItemTypeDto type) {
    return read(ITEM_TYPES, type.getCode(), () -> TmsClient.getItemType(type));
  }

  private static <T> T read(Map<String, T> storage, String key, Supplier<T> getValue) {
    if (!storage.containsKey(key)) {
      storage.put(key, getValue.get());
    }
    return storage.get(key);
  }
}
