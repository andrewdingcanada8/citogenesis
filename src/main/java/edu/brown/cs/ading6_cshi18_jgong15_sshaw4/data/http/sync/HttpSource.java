package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.sync;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.DataSource;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.DataSourceException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public class HttpSource implements DataSource<HttpRequest, HttpResponse<String>, HttpClient> {
  private HttpClient myClient;

  public HttpSource(int timeOutInSec) {
    myClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(timeOutInSec))
        .build();
  }


  @Override
  public HttpResponse<String> runQuery(HttpRequest queryInput) throws DataSourceException {
    try {
      return myClient.send(queryInput, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      throw new DataSourceException(e.getMessage());
    }
  }

  @Override
  public HttpClient getSource() {
    return myClient;
  }
}
