package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.async;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.WebSource;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.QueryCacher;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.async.AsyncHTMLQuery;

import java.util.concurrent.CompletableFuture;

public class AsyncSourceQuery implements Query<String, CompletableFuture<Source>> {
  private Query<String, CompletableFuture<String>> htmlQuery;
  private static final int CACHE_SIZE = 1000;
  public AsyncSourceQuery(int timeOutInSec) {
    htmlQuery = new QueryCacher<>(new AsyncHTMLQuery(timeOutInSec), CACHE_SIZE);
  }

  @Override
  public CompletableFuture<Source> query(String url) throws QueryException {
    CompletableFuture<String> htmlFuture = htmlQuery.query(url);
    return htmlFuture.thenApply(str -> new WebSource(url, str));
  }
}
