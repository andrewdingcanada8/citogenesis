package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.HttpQuery;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Calendar;

public class TimeStampQuery extends HttpQuery<String, Calendar> {

  public TimeStampQuery() {
    super();
  }

  public TimeStampQuery(int timeOutInSec) {
    super(timeOutInSec);
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

  /**
   * Returns a Calendar object with the earliest corresponding time according
   * to URL. Returns null if no entry is found.
   * @param  result from http query
   * @return result Calendar of website timestamp
   */
  @Override
  protected Calendar processResult(HttpResponse<String> result) {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Calendar.class, new CalendarDeserializer());
    Gson gson = gsonBuilder.create();
    return gson.fromJson(result.body(), Calendar.class);
  }
}
