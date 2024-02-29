package org.catools.atlassian.etl.scale.exception;

public class ScaleClientException extends RuntimeException {

  public ScaleClientException(String message, Throwable t) {
    super(message, t);
  }
}
