package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.sync;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Provides abstraction for acquiring webpage HTML.
 */
public class HTMLQuery extends HttpQuery<String, String> {
  private String curURL;

  public HTMLQuery(int timeOutInSec) {
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
  protected String processResult(HttpResponse<String> response) throws QueryException {
    List<String> contentTypes = response.headers().map().get("content-type");
    boolean isHtml = contentTypes
        .stream()
        .anyMatch(str -> str.contains("text/html"));
    if (!isHtml) {
      throw new QueryException(curURL + " is not an html page.");
    }
    return response.body();
  }
}
