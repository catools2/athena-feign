package org.catools.atlassian.etl.jira.translators.parsers;

import com.google.common.collect.ImmutableSet;

import java.util.HashMap;

public interface JiraFieldParser {
  int rank();

  default boolean isDefaultValue(String value) {
    return ImmutableSet.of("0.0", "-1", "{}", "[]", "None", "N/A", String.valueOf(Long.MAX_VALUE)).contains(value);
  }

  boolean isRightParser();

  HashMap<String, String> getNameValuePairs();
}
