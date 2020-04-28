package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.async;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.SendableQuery;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public abstract class AsyncHttpQuery<Q, R> extends SendableQuery
    <Q, CompletableFuture<R>, AsyncHttpSource,
    HttpRequest, CompletableFuture<HttpResponse<String>>, HttpClient> {
  public AsyncHttpQuery(int timeOutInSec) {
    super(new AsyncHttpSource(timeOutInSec));
  }
  public AsyncHttpQuery(int timeOutInSec, String clientKey) {
    super(new AsyncHttpSource(timeOutInSec, clientKey));
  }
}
