package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.async;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.source.DeadSource;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.source.WebSource;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.QueryCacher;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.async.AsyncHTMLQuery;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeoutException;

public class AsyncSourceQuery implements Query<String, CompletableFuture<Source>> {
  private Query<String, CompletableFuture<String>> htmlQuery;
  private static final int CACHE_SIZE = 1000;
  public AsyncSourceQuery(int timeOutInSec) {
    htmlQuery = new QueryCacher<>(new AsyncHTMLQuery(timeOutInSec), CACHE_SIZE);
  }

  @Override
  public CompletableFuture<Source> query(String url) throws QueryException {
    CompletableFuture<String> htmlFuture = htmlQuery.query(url);
    return htmlFuture
        .thenApply(str -> (Source) new WebSource(url, str))
        .exceptionally(ex -> {
          Throwable cause = ex.getCause();
          if (cause instanceof CompletionException
              && cause.getCause() instanceof TimeoutException) {
            System.err.println("Caught AsyncSourceQuery error: " + ex.getMessage());
            return new DeadSource(url, TIMEOUT_ERROR);
          } else if (cause instanceof QueryException) {
            System.err.println("Caught AsyncSourceQuery error: " + ex.getMessage());
            return new DeadSource(url, NOT_HTML_ERROR);
          }
          System.err.println("Caught unexpected AsyncSourceQuery error: " + ex.getMessage());
          return new DeadSource(url, ex.getMessage());
        });
  }
  public static final String TIMEOUT_ERROR = "TIMEOUT";
  public static final String NOT_HTML_ERROR = "NOT HTML PAGE";
}
