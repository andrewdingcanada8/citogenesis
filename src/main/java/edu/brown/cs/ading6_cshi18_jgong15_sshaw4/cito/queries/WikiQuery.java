package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki.Wiki;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.sync.TimeStampQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.sync.HTMLQuery;

import java.util.Calendar;

public class WikiQuery implements Query<String, Wiki> {
  private Query<String, String> htmlQuery;
  private Query<String, Calendar> timeQuery;

  public WikiQuery(int timeOutInSec) {
    htmlQuery = new HTMLQuery(timeOutInSec);
    timeQuery = new TimeStampQuery(timeOutInSec);
  }

  @Override
  public Wiki query(String url) throws QueryException {
    // TODO: figure out image and style support for later
    String html = htmlQuery.query(url);
    Calendar timestamp = timeQuery.query(url);
    return new Wiki(url, html, timestamp);
  }
}
