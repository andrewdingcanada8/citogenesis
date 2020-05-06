package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki.Wiki;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.sync.TimeStampQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.sync.HTMLQuery;

import java.util.Calendar;

/**
 * Query class for Wiki.
 */
public class WikiQuery implements Query<String, Wiki> {
  private Query<String, String> htmlQuery;
  private Query<String, Calendar> timeQuery;
  private Integer timeOutInSec;

  /**
   * Constructor to query the Wiki.
   * @param timeOutInSec the timeout in seconds.
   */
  public WikiQuery(int timeOutInSec) {
    htmlQuery = new HTMLQuery(timeOutInSec);
    timeQuery = new TimeStampQuery(timeOutInSec);
    this.timeOutInSec = timeOutInSec;
  }

  /**
   * Query to get the Wiki class.
   * @param url url of the wiki page.
   * @return the Wiki class
   * @throws QueryException exception that occurs during query.
   */
  @Override
  public Wiki query(String url) throws QueryException {
    String html = htmlQuery.query(url);
    Calendar timestamp = timeQuery.query(url);
    Integer timeOut = timeOutInSec;
    return new Wiki(url, html, timestamp, timeOut);
  }
}
