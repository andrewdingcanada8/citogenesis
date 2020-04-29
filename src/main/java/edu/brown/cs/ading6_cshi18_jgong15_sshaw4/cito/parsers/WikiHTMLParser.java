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
  }

  public WikiHTMLParser(String url, String html) {
    this.url = url;
    this.html = html;
    this.timestamp = null;
  }

  public Set<Citation> parseForRawCitation() {
    Set<Citation> uncheckedCitations = new HashSet<>();
    Document doc = Jsoup.parse(html, url);
    // Parses out the superscript citations in text
    Elements refs = doc.select("sup.reference > a");
    for (Element ref : refs) {
      // Parses out the id used for the end of the wiki page reference
      String citeNote = ref.attr("href");
      Element prev = ref.parent().parent();
      String contentCited = prev.text();
      Elements refContent = doc.select(citeNote);
      System.out.println(refContent);
      Elements extLinks = refContent.select(".external");
      Element timeAccessed = refContent.select(".reference-accessdate").first();
      // If there are more than one external links:
      // Use the first archive one for link; the second one is the unaccessble original
      if (extLinks != null) {
        Element extLink = extLinks.first();


      } else {
        uncheckedCitations.add(new Citation("Other"));
      }

    }
    return uncheckedCitations;
  }

  public Set<Citation> parseForCitations() {
    Set<Citation> checkedCitations = new HashSet<>();
    Set<Citation> uncheckedCitations = parseForRawCitation();
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

  private static void log(String msg, String... vals) {
    System.out.println(String.format(msg, vals));
  }

}
