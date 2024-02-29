package org.catools.atlassian.etl.scale.rest.cycle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public enum ScaleExecutionStatus {
  UNSET(null),
  IN_PROGRESS("In Progress"),
  FAIL("Fail"),
  BLOCKED("Blocked"),
  PASS("Pass"),
  NOT_EXECUTED("Not Executed");

  @Getter
  private final String scaleName;

  ScaleExecutionStatus(String scaleName) {
    this.scaleName = scaleName;
  }

  @JsonCreator
  public static ScaleExecutionStatus formScaleName(String value) {
    return Set.of(values()).stream().filter(s -> StringUtils.equalsIgnoreCase(s.getScaleName(), value)).findFirst().orElse(null);
  }

  @JsonValue
  public String getScaleName() {
    return scaleName;
  }
}