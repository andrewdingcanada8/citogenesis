package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import java.util.Calendar;
import java.util.Set;

public class Wiki {
  private Set<Citation> citationSet;
  private String url;
  private String html; // TODO: find the best representation for html

  public Wiki() {
    citationSet = null;
  }

  public Wiki(String url, String html, Calendar timestamp) {
    this.url = url;
    this.html = html;
    WikiHTMLParser wikiHTMLParser = new WikiHTMLParser(url, html, timestamp);
    citationSet = wikiHTMLParser.parseForCitations();
  }

  public Set<Citation> getCitationSet() {
    return citationSet;
  }
}
