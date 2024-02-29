package org.catools.atlassian.etl.scale.translators;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.core.model.*;
import org.catools.athena.rest.feign.tms.cache.CacheManager;
import org.catools.athena.tms.model.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@UtilityClass
public class TranslatorHelper {
  private static final String UNSET = "UNSET";

  public static String getUser(String username) {
    if (username == null) {
      return null;
    }
    return CacheManager.readUser(new UserDto(username)).getUsername();
  }

  public static String getVersion(String version, String projectCode) {
    return version == null || StringUtils.isBlank(version) ?
           UNSET :
           CacheManager.readVersion(new VersionDto(generateCode(version), version, projectCode)).getCode();
  }

  public static MetadataDto getMetaData(String name, String value) {
    return new MetadataDto(name, value);
  }

  public static String getItemType(String issueType) {
    return issueType == null || StringUtils.isBlank(issueType) ?
           UNSET :
           CacheManager.readType(new ItemTypeDto(generateCode(issueType), issueType)).getCode();
  }

  public static String getPriority(String priority) {
    return priority == null || StringUtils.isBlank(priority) ?
           UNSET :
           CacheManager.readPriority(new PriorityDto(generateCode(priority), priority)).getCode();
  }

  public static String getStatus(String statusName) {
    return StringUtils.isBlank(statusName) ? UNSET : CacheManager.readStatus(new StatusDto(generateCode(statusName), statusName)).getCode();
  }

  private static String generateCode(String name) {
    String[] split = name.split(" ");
    int tokenSize = Math.max(2, (10 / split.length));

    return Stream.of(split).map(s -> s.substring(0, Math.min(tokenSize, s.length()))).collect(Collectors.joining()).toUpperCase();
  }
}
