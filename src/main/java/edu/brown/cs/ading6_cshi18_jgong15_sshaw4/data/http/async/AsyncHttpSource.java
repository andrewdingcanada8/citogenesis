package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.async;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.DataSource;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Semaphore;

public class AsyncHttpSource implements DataSource<HttpRequest,
    CompletableFuture<HttpResponse<String>>,
    HttpClient> {
  private static volatile Map<Integer, HttpClient> clients;
  private static volatile Map<Integer, Semaphore> semaphores;
  private static final int NUM_STREAMS = 10;
  private HttpClient myClient;
  private Semaphore mySem;

  public AsyncHttpSource(int timeOutInSec) {
    // initialize client map if called for the first time
    if (clients == null) {
      clients = new HashMap<>();
    }

    if (semaphores == null) {
      semaphores = new HashMap<>();
    }

    // check if we have any clients with the corresponding timeout
    boolean noClient = clients.keySet().stream().noneMatch(n -> n == timeOutInSec);
    if (noClient) {
      clients.put(timeOutInSec,
          HttpClient.newBuilder()
              .connectTimeout(Duration.ofSeconds(timeOutInSec))
              .build());
      semaphores.put(timeOutInSec, new Semaphore(NUM_STREAMS));
    }
    myClient = clients.get(timeOutInSec);
    mySem = semaphores.get(timeOutInSec);
  }


  @Override
  public CompletableFuture<HttpResponse<String>> runQuery(HttpRequest queryInput) {
    mySem.acquireUninterruptibly();
    return myClient.sendAsync(queryInput, HttpResponse.BodyHandlers.ofString())
        .thenApply(res -> {
          mySem.release();  // release when done obtaining request info
          System.out.println("released, num left: " + mySem.availablePermits());
          return res;
        }).exceptionally(e -> {
          mySem.release();
          throw new CompletionException(e);
        });
  }

  @Override
  public HttpClient getSource() {
    return myClient;
  }
}
