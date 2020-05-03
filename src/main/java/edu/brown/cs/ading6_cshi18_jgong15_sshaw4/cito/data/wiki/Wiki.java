package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
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

/**
 * Wiki class to represent the input Wiki page.
 * Also implements Source to work with web search.
 */
public class Wiki implements Source {
  private WikiHTMLParser parser;
  private Set<String> citationIDs;
  private Set<Citation> citationSet;
  private Calendar timestamp;
  private String url;
  private String html;
  private String title;
  private String content;
  private String contentHTML;
  private AsyncHttpQuery<String, Calendar> timestampQuery;
  private CompletableFuture<Calendar> calFut;

  /**
   * Constructor for Wiki that has the timestamp.
   * @param url url of the wiki page
   * @param html html of the wiki page
   * @param timestamp timestamp
   * @param timeout timeout parameter for queries
   */
  public Wiki(String url, String html, Calendar timestamp, Integer timeout) {
    this.url = url;
    this.html = html;
    this.timestamp = timestamp;
    // initialize query
    timestampQuery = new AsyncTimeStampQuery(timeout);
    // initialize parser
    parser = new WikiHTMLParser(url, html, timestamp);
  }

  /**
   * Constructor for Wiki without the timestamp.
   * @param url url of the wiki page
   * @param html html of the wiki page
   * @param timeout timeout parameter for queries
   */
  public Wiki(String url, String html, Integer timeout) {
    this.url = url;
    this.html = html;
    // initialize query
    timestampQuery = new AsyncTimeStampQuery(timeout);
    // initialize parser
    parser = new WikiHTMLParser(url, html);
  }

  /**
   * Get title of the wiki page.
   * @return string of the title.
   */
  @Override
  public String title() {
    if (title == null) {
      title = parser.parserForTitle();
    }
    return title;
  }

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
   * Text of the wiki page content-body.
   *
   * @return content in string
   */
  @Override
  public String getContent() {
    if (content == null) {
      content = parser.parseForContent();
    }
    return content;
  }

  /**
   * Get html within content-body.
   * @return string of the html.
   */
  public String getContentHTML() {
    if (contentHTML == null) {
      contentHTML = parser.parseForContentHTML();
    }
    return contentHTML;
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
   * Query the set of citation ids.
   */
  public void queryCitationIDs() {
    if (citationIDs != null) {
      citationIDs = parser.parseForCitationIDs();
    }
  }

  /**
   * Returns the set of citation ids.
   * @return set of citation ids.
   */
  public Set<String> getCitationIDs() {
    if (citationIDs == null) {
      queryCitationIDs();
    }
    return citationIDs;
  }

  /**
   * Returns a citation from the corresponding id.
   * @param id citation id.
   * @return a citation.
   */
  public Citation getCitationFromID(String id) {
    Citation citation = parser.parserForCitationFromID(id);
    if (citation != null) {
      return citation;
    } else {
      System.out.println("ERROR: Cannot find citation from id.");
      return null;
    }
  }

  /**
   * Queries the entire citation set. Expensive.
   */
  public void queryCitationSet() {
    if (citationSet != null) {
      citationSet = parser.parseForRawCitations();
    }
  }

  /**
   * Get the total set of citations.
   * @return set of citations.
   */
  public Set<Citation> getCitationSet() {
    if (citationSet == null) {
      queryCitationSet();
    }
    return citationSet;
  }
}
