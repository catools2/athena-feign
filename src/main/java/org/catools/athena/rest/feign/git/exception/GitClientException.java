package org.catools.athena.rest.feign.git.exception;


public class GitClientException extends RuntimeException {

  public GitClientException(String message, Throwable t) {
    super(message, t);
  }

}
