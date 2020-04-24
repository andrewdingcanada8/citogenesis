package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.DataSource;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.DataSourceException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class HttpSource implements DataSource<HttpRequest, HttpResponse<String>, HttpClient> {
  private static volatile Map<Integer, HttpClient> clients;
  private HttpClient myClient;

  public HttpSource(int timeOutInSec) {
    // initialize client map if called for the first time
    if (clients == null) {
      clients = new HashMap<>();
    }

    // check if we have any clients with the corresponding timeout
    boolean noClient = clients.keySet().stream().noneMatch(n -> n == timeOutInSec);
    if (noClient) {
      clients.put(timeOutInSec,
          HttpClient.newBuilder()
              .connectTimeout(Duration.ofSeconds(timeOutInSec))
              .build());
    }
    myClient = clients.get(timeOutInSec);
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
