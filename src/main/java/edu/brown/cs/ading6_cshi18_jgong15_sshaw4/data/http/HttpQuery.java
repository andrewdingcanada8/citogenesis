package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.SendableQuery;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class HttpQuery<Q, R>
    extends SendableQuery<Q, R, HttpSource, HttpRequest, HttpResponse<String>, HttpClient> {
  public HttpQuery(int timeOutInSec) {
    super(new HttpSource(timeOutInSec));
  }
}
