package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CheckConnectionQuery extends HttpQuery<String, Boolean> {

  public CheckConnectionQuery() {
    super();
  }

  public CheckConnectionQuery(int timeOutInSec) {
    super(timeOutInSec);
  }

  @Override
  protected HttpRequest getQuery(String input, HttpClient src) {
    return HttpRequest.newBuilder()
        .uri(URI.create(input))
        .build();
  }

  @Override
  protected Boolean processResult(HttpResponse<String> result) throws QueryException {
    return true;
  }
}
