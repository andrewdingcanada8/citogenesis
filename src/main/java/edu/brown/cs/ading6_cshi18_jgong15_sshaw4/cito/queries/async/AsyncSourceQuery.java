package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.async;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.WebSource;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.async.AsyncHTMLQuery;

import java.util.Calendar;
import java.util.concurrent.CompletableFuture;

public class AsyncSourceQuery implements Query<String, CompletableFuture<Source>> {
  private Query<String, CompletableFuture<String>> htmlQuery;
  private Query<String, CompletableFuture<Calendar>> timeQuery;

  public AsyncSourceQuery(int timeOutInSec) {
    htmlQuery = new AsyncHTMLQuery(timeOutInSec);
    timeQuery = new AsyncTimeStampQuery(timeOutInSec);
  }

  @Override
  public CompletableFuture<Source> query(String url) throws QueryException {
    CompletableFuture<String> htmlFuture = htmlQuery.query(url);
    //CompletableFuture<Calendar> timeFuture = timeQuery.query(url);
    //return htmlFuture.thenCombineAsync(timeFuture, (html, time) -> new WebSource(url, html, time));
    return htmlFuture.thenCompose(str ->
        CompletableFuture.supplyAsync(() ->
            new WebSource(url, str, null)));
  }
}
