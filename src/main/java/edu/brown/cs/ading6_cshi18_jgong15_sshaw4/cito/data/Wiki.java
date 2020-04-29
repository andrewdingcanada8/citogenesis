package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.parsers.WikiHTMLParser;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.async.AsyncTimeStampQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.async.AsyncHttpQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class Wiki implements Source {
  public static final int QUERY_TIMEOUT = 10;
  private Set<Citation> citationSet = new HashSet<>();
  private Calendar timestamp;
  private String url;
  private String html;
  private String content;
  private AsyncHttpQuery<String, Calendar> timestampQuery;
  private CompletableFuture<Calendar> calFut;

  /**
   * Get webpage html.
   *
   * @return HTML
   */
  @Override
  public String getHTML() {
    return html;
  }

  /**
   * Get parsed content of the source.
   *
   * @return content
   */
  @Override
  // TODO: Finish this
  public String getContent() {
    return content;
  }

  /**
   * Get webpage url.
   *
   * @return URL
   */
  @Override
  public String getURL() {
    return url;
  }

  /**
   * Return all hrefs.
   *
   * @return hrefs
   */
  @Override
  public List<String> getLinks() {
    List<String> links = new ArrayList<>();
    for (Citation citation : citationSet) {
      if (citation.getSourceType().equals("Web")) {
        links.add(citation.getInitialWebSource().getURL());
      }
    }
    return links;
  }

  /**
   * Set off timestamp query process (if needed).
   */
  @Override
  public void queryTimestamp() {
    if (timestamp != null) {
      return;
    }
    try {
      calFut = timestampQuery.query(url).exceptionally(e -> {
        System.out.println("Error while querying timestamp for " + url + ": "
            + e.getMessage());
        return null;
      });
    } catch (QueryException e) {
      System.out.println("Error while initiating query for calendar for "
          + url + ": " + e.getMessage());
      calFut = null;
    }
  }

  /**
   * Get timestamp of the source.
   *
   * @return timestamp
   */
  @Override
  public Calendar getTimestamp() {
    if (timestamp == null && calFut != null) {
      timestamp = calFut.join();
    }
    // if previous kick failed, try one more time, synchronously
    this.queryTimestamp();
    if (timestamp == null && calFut != null) {
      timestamp = calFut.join();
    }
    // will return null of both tries error
    return timestamp;
  }

  /**
   * Constructor for Wiki that has the timestamp.
   * @param url url of the wiki page
   * @param html html of the wiki page
   * @param timestamp timestamp
   */
  public Wiki(String url, String html, Calendar timestamp) {
    this.url = url;
    this.html = html;
    this.timestamp = timestamp;
    // initialize query
    timestampQuery = new AsyncTimeStampQuery(QUERY_TIMEOUT);
    WikiHTMLParser wikiHTMLParser = new WikiHTMLParser(url, html, timestamp);
    citationSet = wikiHTMLParser.parseForRawCitations();
    content = wikiHTMLParser.parseForContent();
  }

  public Wiki(String url, String html) {
    this.url = url;
    this.html = html;
    // initialize query
    timestampQuery = new AsyncTimeStampQuery(QUERY_TIMEOUT);
    WikiHTMLParser wikiHTMLParser = new WikiHTMLParser(url, html);
    citationSet = wikiHTMLParser.parseForCitations();
    content = wikiHTMLParser.parseForContent();
  }

  public Set<Citation> getCitationSet() {
    return citationSet;
  }
}
