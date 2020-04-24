package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.async;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class AsyncHTMLQuery extends AsyncHttpQuery<String, String> {
  private String curURL;

  public AsyncHTMLQuery(int timeOutInSec) {
    super(timeOutInSec);
    curURL = "UNUSED";
  }

  @Override
  protected HttpRequest getQuery(String url, HttpClient src) {
    curURL = url;
    return HttpRequest.newBuilder()
        .uri(URI.create(url))
        .build();
  }

  @Override
  protected CompletableFuture<String> processResult(
      CompletableFuture<HttpResponse<String>> result) {
    return result.thenCompose(res ->
        CompletableFuture.supplyAsync(() -> {
          List<String> contentTypes = res.headers().map().get("content-type");
          try {
            boolean isHtml = contentTypes
                .stream()
                .anyMatch(str -> str.contains("text/html"));
            if (!isHtml) {
              throw new CompletionException(new QueryException(curURL + " is not an html page."));
            }
          } catch (NullPointerException e) {
            throw new CompletionException(new QueryException("cannot obtain headers"));
          }
          return res.body();
        }));
  }
}
