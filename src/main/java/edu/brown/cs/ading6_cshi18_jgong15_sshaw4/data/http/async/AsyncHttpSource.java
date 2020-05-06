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
import java.util.concurrent.TimeUnit;

public class AsyncHttpSource implements DataSource<HttpRequest,
    CompletableFuture<HttpResponse<String>>,
    HttpClient> {
  private static volatile Map<String, HttpClient> clients;
  private static volatile Map<String, Semaphore> semaphores;
  private static volatile Map<String, Integer> sslTimeouts;
  private static final int NUM_STREAMS = 15;
  private HttpClient myClient;
  private Semaphore mySem;
  private int myTimeout;

  public AsyncHttpSource(int myTimeoutInSec) {
    this(myTimeoutInSec, "default");
  }

  public AsyncHttpSource(int timeOutInSec, String clientKey) {
    // initialize client map if called for the first time
    if (clients == null) {
      clients = new HashMap<>();
    }

    if (semaphores == null) {
      semaphores = new HashMap<>();
    }

    if (sslTimeouts == null) {
      sslTimeouts = new HashMap<>();
    }

    // check if we have any clients with the key
    boolean hasClient = clients.keySet().stream().anyMatch(n -> n.equals(clientKey));
    if (!hasClient) {
      clients.put(clientKey,
          HttpClient.newBuilder()
              .connectTimeout(Duration.ofSeconds(timeOutInSec))
              .build());
      semaphores.put(clientKey, new Semaphore(NUM_STREAMS));
      sslTimeouts.put(clientKey, timeOutInSec);
    }
    myClient = clients.get(clientKey);
    mySem = semaphores.get(clientKey);
    myTimeout = sslTimeouts.get(clientKey);
  }


  @Override
  public CompletableFuture<HttpResponse<String>> runQuery(HttpRequest queryInput) {
    mySem.acquireUninterruptibly();
    //System.err.println("acquiring permit. Num left: " + mySem.availablePermits());
    return myClient.sendAsync(queryInput, HttpResponse.BodyHandlers.ofString())
        .orTimeout(myTimeout, TimeUnit.SECONDS) // due to SSL infinite loop bug, implement external timeout
        .thenApply(res -> {
          mySem.release();  // release when done obtaining request info
          //System.err.println("released, num left: " + mySem.availablePermits());
          return res;
        }).exceptionally(e -> {
          mySem.release();  // if exception is hit (specfically timeout), release semaphore anyway
          throw new CompletionException(e);
        });
  }

  @Override
  public HttpClient getSource() {
    return myClient;
  }
}
