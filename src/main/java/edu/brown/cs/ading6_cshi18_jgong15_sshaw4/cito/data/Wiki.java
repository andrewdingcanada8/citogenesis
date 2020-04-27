package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.parsers.WikiHTMLParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Wiki implements Source {
  private Set<Citation> citationSet = new HashSet<>();
  private Calendar timestamp;
  private String url;
  private String html; // TODO: find the best representation for html

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
    return null;
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
   * Get timestamp of the source.
   *
   * @return timestamp
   */
  @Override
  public Calendar getTimestamp() {
    return timestamp;
  }

  public Wiki(String url, String html, Calendar timestamp) {
    this.url = url;
    this.html = html;
    this.timestamp = timestamp;
//    WikiHTMLParser wikiHTMLParser = new WikiHTMLParser(url, html, timestamp);
//    citationSet = wikiHTMLParser.parseForCitations();
  }

  public Set<Citation> getCitationSet() {
    return citationSet;
  }
}
