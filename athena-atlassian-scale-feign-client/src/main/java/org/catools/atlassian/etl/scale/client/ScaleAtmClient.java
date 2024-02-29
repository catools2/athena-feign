package org.catools.atlassian.etl.scale.client;

import feign.Param;
import feign.RequestLine;
import org.catools.atlassian.etl.scale.model.*;

import java.util.HashSet;
import java.util.Set;


public interface ScaleAtmClient {
  @RequestLine("GET /testrun/search?startAt={startAt}&maxResults={maxResults}&query={query}")
  Set<ScaleTestRun> searchTestRun(
      @Param("startAt")
      int startAt,
      @Param("maxResults")
      int maxResults,
      @Param("query")
      String query);

  @RequestLine("GET /testcase/search?startAt={startAt}&maxResults={maxResults}&query={query}&fields={fields}")
  Set<ScaleTestRun> searchTestRun(
      @Param("startAt")
      int startAt,
      @Param("maxResults")
      int maxResults,
      @Param("fields")
      String fields,
      @Param("query")
      String query);

  @RequestLine("GET /testcase/search?startAt={startAt}&maxResults={maxResults}&query={query}")
  Set<ScaleTestCase> searchTestCase(
      @Param("startAt")
      int startAt,
      @Param("maxResults")
      int maxResults,
      @Param("query")
      String query);

  @RequestLine("GET /testcase/search?startAt={startAt}&maxResults={maxResults}&query={query}&fields={fields}")
  Set<ScaleTestCase> searchTestCase(
      @Param("startAt")
      int startAt,
      @Param("maxResults")
      int maxResults,
      @Param("fields")
      String fields,
      @Param("query")
      String query);

  @RequestLine("GET /testcase/{key}")
  ScaleTestCase getTestCase(
      @Param("key")
      String key);

  @RequestLine("GET /testrun/{key}")
  ScaleTestRun getTestRun(
      @Param("key")
      String key);

  @RequestLine("GET /testrun/{key}/testresults?startAt={startAt}&maxResults={maxResults}")
  HashSet<ScaleTestResult> getTestResults(
      @Param("startAt")
      int startAt,
      @Param("maxResults")
      int maxResults);

}
