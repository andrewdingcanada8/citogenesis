package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.parsers;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Citation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Parser to parse requests from the wiki class.
 */
public class WikiHTMLParser {
  /**
   * Set of citations that haven't been checked for revision timestamps.
   */
  private Set<Citation> uncheckedCitations;
  /**
   * URL of wiki to parse.
   */
  private String url;
  /**
   * HTML of wiki to parse.
   */
  private String html;
  /**
   * Access timestamp of wiki to parse.
   */
  private Calendar timestamp;

  public WikiHTMLParser(String url, String html, Calendar timestamp) {
    this.url = url;
    this.html = html;
    this.timestamp = timestamp;
    uncheckedCitations = null;
  }

  public void parseForRawCitation() {
    Document doc = Jsoup.parse(html, url);
    Elements refs = doc.select(".reference");
    System.out.println(refs);
//    for (Element ref : refs) {
//      Citation
//    }
  }

  public Set<Citation> parseForCitations() {
    Set<Citation> checkedCitations = new HashSet<>();
    parseForRawCitation();
    for (Citation citation : uncheckedCitations) {
      if (!checkCitation(citation)) {
        citation.setSourceType("Self");
      }
      checkedCitations.add(citation);
    }
    return checkedCitations;
  }

  /**
   * Return true if cited content of the citation is not present
   * before citation is made.
   * @param citation citation to check
   * @return true or false
   */
  public boolean checkCitation(Citation citation) {
    // TODO: check revision timestamp
    return true;
  }
}
