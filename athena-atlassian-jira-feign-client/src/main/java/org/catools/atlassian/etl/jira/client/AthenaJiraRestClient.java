package org.catools.atlassian.etl.jira.client;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClient;
import com.atlassian.jira.rest.client.internal.async.DisposableHttpClient;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class AthenaJiraRestClient extends AsynchronousJiraRestClient implements JiraRestClient {

  private final SearchRestClient searchRestClient;

  public AthenaJiraRestClient(URI serverUri, DisposableHttpClient httpClient) {
    super(serverUri, httpClient);
    final URI baseUri = UriBuilder.fromUri(serverUri).path("/rest/api/latest").build();
    searchRestClient = new AthenaSearchRestClient(baseUri, httpClient);
  }

  @Override
  public SearchRestClient getSearchClient() {
    return searchRestClient;
  }
}
