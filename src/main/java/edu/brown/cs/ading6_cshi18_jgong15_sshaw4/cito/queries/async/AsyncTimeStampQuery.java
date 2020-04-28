package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.async;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.CalendarDeserializer;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.async.AsyncHttpQuery;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Calendar;
import java.util.concurrent.CompletableFuture;

public class AsyncTimeStampQuery extends AsyncHttpQuery<String, Calendar> {

  private static final Gson CAL_GSON;

  static {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Calendar.class, new CalendarDeserializer());
    gsonBuilder.registerTypeAdapter(Calendar.class, new CalendarDeserializer());
    CAL_GSON = gsonBuilder.create();
  }

  public AsyncTimeStampQuery(int timeOutInSec) {
    super(timeOutInSec, "cito_timestamp");
  }

  @Override
  protected HttpRequest getQuery(String input, HttpClient src) {

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://web.archive.org/cdx/search/cdx?url="
            + input
            + "&fl=timestamp&output=json&limit=1"))
        .build();
    return request;
  }

  @Override
  protected CompletableFuture<Calendar> processResult(
      CompletableFuture<HttpResponse<String>> result) {
    return result.thenCompose(res -> CompletableFuture.supplyAsync(
        () -> CAL_GSON.fromJson(res.body(), Calendar.class)));
  }
}
