package org.catools.atlassian.etl.jira.exception;

public class JiraClientException extends RuntimeException {

  public JiraClientException(String message, Throwable t) {
    super(message, t);
  }
}
