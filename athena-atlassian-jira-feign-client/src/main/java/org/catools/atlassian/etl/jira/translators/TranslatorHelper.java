package org.catools.atlassian.etl.jira.translators;

import com.atlassian.jira.rest.client.api.domain.*;
import com.google.common.collect.Sets;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.core.model.*;
import org.catools.athena.rest.feign.tms.cache.CacheManager;
import org.catools.athena.tms.model.*;
import org.catools.atlassian.etl.jira.translators.parsers.JiraParser;
import org.codehaus.jettison.json.JSONObject;

import java.util.*;
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

  public static String getUser(BasicUser user) {
    if (user == null) {
      return null;
    }

    Set<UserAliasDto> aliases = Set.of(new UserAliasDto(user.getName()), new UserAliasDto(user.getDisplayName()));
    return CacheManager.readUser(new UserDto(user.getName(), aliases)).getUsername();
  }

  public static void addIssueMetaData(Issue issue, ItemDto item, List<String> fieldsToRead) {
    for (BasicComponent component : issue.getComponents()) {
      item.getMetadata().add(getMetaData("Component", component.getName()));
    }

    if (issue.getAssignee() != null && StringUtils.isNotBlank(issue.getAssignee().getEmailAddress())) {
      item.getMetadata().add(getMetaData("Assignee", issue.getAssignee().getEmailAddress()));
    }

    if (issue.getLabels() != null) {
      for (String label : issue.getLabels()) {
        item.getMetadata().add(getMetaData("Label", label));
      }
    }

    if (issue.getFields() != null) {
      Set<IssueField> noneNull = Sets.newHashSet(issue.getFields().iterator())
                                     .stream()
                                     .filter(TranslatorHelper::fieldIsNotNull)
                                     .collect(Collectors.toSet());
      for (IssueField field : noneNull) {
        Stream<Map.Entry<String, String>> fieldsToSync = JiraParser.parserJiraField(field).entrySet().stream();

        if (!fieldsToRead.isEmpty()) {
          fieldsToSync = fieldsToSync.filter(e -> fieldsToRead.contains(e.getKey()));
        }

        fieldsToSync.forEach(e -> item.getMetadata().add(getMetaData(e.getKey(), e.getValue())));
      }
    }
  }

  public static String getVersion(String version, String projectKey) {
    return version == null || StringUtils.isBlank(version) ?
           UNSET :
           CacheManager.readVersion(new VersionDto(generateCode(version), version, projectKey)).getCode();
  }

  public static String getVersion(Version version, String projectKey) {
    return version == null ? UNSET : getVersion(version.getName(), projectKey);
  }

  public static Set<String> getIssueVersions(Issue issue, String projectKey) {
    Set<String> versions = new HashSet<>();

    if (issue.getFixVersions() != null) {
      for (Version fixVersion : issue.getFixVersions()) {
        versions.add(getVersion(fixVersion, projectKey));
      }
    }

    if (issue.getAffectedVersions() != null) {
      for (Version fixVersion : issue.getAffectedVersions()) {
        versions.add(getVersion(fixVersion, projectKey));
      }
    }

    return versions;
  }

  public static MetadataDto getMetaData(String name, String value) {
    return new MetadataDto(name, value);
  }

  public static String getProject(BasicProject project) {
    return project == null || StringUtils.isBlank(project.getName()) ?
           UNSET :
           CacheManager.readProject(new ProjectDto(project.getKey(), project.getName())).getCode();
  }

  public static String getItemType(IssueType issueType) {
    return issueType == null ? UNSET : getItemType(issueType.getName());
  }

  public static String getItemType(String issueType) {
    return issueType == null || StringUtils.isBlank(issueType) ?
           UNSET :
           CacheManager.readType(new ItemTypeDto(generateCode(issueType), issueType)).getCode();
  }

  public static String getPriority(BasicPriority priority) {
    return priority == null ? UNSET : getPriority(priority.getName());
  }

  public static String getPriority(String priority) {
    return priority == null || StringUtils.isBlank(priority) ?
           UNSET :
           CacheManager.readPriority(new PriorityDto(generateCode(priority), priority)).getCode();
  }

  public static String getStatus(String statusName) {
    return StringUtils.isBlank(statusName) ? UNSET : CacheManager.readStatus(new StatusDto(generateCode(statusName), statusName)).getCode();
  }

  public static String getStatus(Status status) {
    return status == null || StringUtils.isBlank(status.getName()) ?
           UNSET :
           CacheManager.readStatus(new StatusDto(generateCode(status.getName()), status.getName())).getCode();
  }

  public static boolean fieldIsNotNull(IssueField f) {
    return f.getValue() != null && f.getValue() != JSONObject.EXPLICIT_NULL && f.getValue() != JSONObject.NULL;
  }

  private static String generateCode(String name) {
    String[] split = name.split(" ");
    int tokenSize = Math.max(2, (10 / split.length));

    return Stream.of(split).map(s -> s.substring(0, Math.min(tokenSize, s.length()))).collect(Collectors.joining()).toUpperCase();
  }
}
