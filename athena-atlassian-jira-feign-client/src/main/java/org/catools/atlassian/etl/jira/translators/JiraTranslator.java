package org.catools.atlassian.etl.jira.translators;

import com.atlassian.jira.rest.client.api.domain.*;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.tms.model.ItemDto;
import org.catools.athena.tms.model.StatusTransitionDto;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.catools.atlassian.etl.jira.translators.TranslatorHelper.*;

@Slf4j
public class JiraTranslator {

  public static ItemDto translateIssue(Issue issue, String projectKey, List<String> fieldsToRead) {
    Objects.requireNonNull(issue);

    ItemDto item = new ItemDto();
    item.setCode(issue.getKey());

    item.setProject(getProject(issue.getProject()));
    item.setVersions(getIssueVersions(issue, projectKey));
    item.setStatus(getStatus(issue.getStatus()));
    item.setPriority(getPriority(issue.getPriority()));
    item.setType(getItemType(issue.getIssueType()));
    item.setName(StringUtils.substring(issue.getSummary(), 0, 1000));
    item.setCreatedOn(issue.getCreationDate().toDate().toInstant());
    item.setCreatedBy(getUser(issue.getReporter()));
    item.setUpdatedOn(issue.getUpdateDate() == null ? null : issue.getUpdateDate().toDate().toInstant());

    item.getMetadata().clear();
    addIssueMetaData(issue, item, fieldsToRead);

    item.getStatusTransitions().clear();
    addStatusTransition(issue, item);

    item.getStatusTransitions()
        .stream()
        .filter(st -> st.getAuthor() != null)
        .min(Comparator.comparing(StatusTransitionDto::getOccurred))
        .ifPresent(st -> item.setUpdatedBy(getUser(st.getAuthor())));

    log.trace("translate issue:\n {} \nto:\n {}", issue, item);

    return item;
  }

  private static void addStatusTransition(Issue issue, ItemDto item) {
    if (issue.getChangelog() != null) {
      for (ChangelogGroup changelog : issue.getChangelog()) {
        if (changelog == null || changelog.getAuthor() == null) {
          continue;
        }

        List<ChangelogItem> transitions = Sets.newHashSet(changelog.getItems().iterator())
                                              .stream()
                                              .filter(f -> f != null && StringUtils.equalsIgnoreCase(f.getField(), "status"))
                                              .collect(Collectors.toList());

        String author = getUser(changelog.getAuthor());

        for (ChangelogItem statusChangelog : transitions) {
          if (statusChangelog == null) {
            continue;
          }

          Instant occurred = changelog.getCreated() == null ? null : changelog.getCreated().toDate().toInstant();
          String from = getStatus(statusChangelog.getFromString());
          String to = getStatus(statusChangelog.getToString());
          item.getStatusTransitions().add(new StatusTransitionDto(from, to, author, occurred));
        }
      }
    }
  }

}
