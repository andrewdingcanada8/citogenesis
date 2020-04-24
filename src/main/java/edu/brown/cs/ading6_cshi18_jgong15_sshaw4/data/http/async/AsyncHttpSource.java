package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.async;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.DataSource;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AsyncHttpSource implements DataSource<HttpRequest,
    CompletableFuture<HttpResponse<String>>,
    HttpClient> {
  private static volatile Map<Integer, HttpClient> clients;
  private HttpClient myClient;

  public AsyncHttpSource(int timeOutInSec) {
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
  public CompletableFuture<HttpResponse<String>> runQuery(HttpRequest queryInput) {
    return myClient.sendAsync(queryInput, HttpResponse.BodyHandlers.ofString());
  }

  @Override
  public HttpClient getSource() {
    return myClient;
  }
}
