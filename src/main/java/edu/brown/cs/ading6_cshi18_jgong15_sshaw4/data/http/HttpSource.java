package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.DataSource;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.DataSourceException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpSource implements DataSource<HttpRequest, HttpResponse<String>, HttpClient> {
  private HttpClient client;
  public HttpSource() {
    client = HttpClient.newHttpClient();
  }
  @Override
  public HttpResponse<String> runQuery(HttpRequest queryInput) throws DataSourceException {
    try {
      return client.send(queryInput, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      throw new DataSourceException(e.getMessage());
    }
  }

  @Override
  public HttpClient getSource() {
    return client;
  }
}
