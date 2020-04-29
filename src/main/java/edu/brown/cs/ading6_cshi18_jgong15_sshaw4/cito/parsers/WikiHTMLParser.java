package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.parsers;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Citation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

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

  /**
   * Constructor for parser with timestamp.
   * @param url url of the wiki page
   * @param html html of the wiki
   * @param timestamp timestamp of the wiki page
   */
  public WikiHTMLParser(String url, String html, Calendar timestamp) {
    this.url = url;
    this.html = html;
    this.timestamp = timestamp;
  }

  /**
   * Constructor for parser.
   * @param url url of the wiki page.
   * @param html html of the wiki
   */
  public WikiHTMLParser(String url, String html) {
    this.url = url;
    this.html = html;
    this.timestamp = null;
  }

  public String parseForContent() {
    Document doc = Jsoup.parse(html, url);
    Elements content = doc.select("#content");
    if (content != null) {
      return content.text();
    } else {
      return "";
    }
  }

  public String parseForContentHTML() {
    Document doc = Jsoup.parse(html, url);
    Elements content = doc.select("#content");
    if (content != null) {
      return content.html();
    } else {
      return "";
    }
  }

  /**
   * Parse the wiki html for citations.
   * @return a set of citation that hasn't been checked for revision date
   *
   */
  public Set<Citation> parseForRawCitations() {
    Set<Citation> uncheckedCitations = new HashSet<>();
    Document doc = Jsoup.parse(html, url);
    // Parses out the superscript citations in text
    Elements refs = doc.select("sup.reference > a");
    for (Element ref : refs) {
      // Parses out the id used for the end of the wiki page reference
      String citeNote = ref.attr("href");
      Element prev = ref.parent().parent();
      String citedContent = prev.text();
      Elements refContent = doc.select(citeNote);
      //System.out.println(refContent);
      Elements extLinks = refContent.select(".external");
      Element timeAccessed = refContent.select(".reference-accessdate").first();
      // Check if the citation is already made with another instance of cited content.
      Predicate<Citation> isQualified = elt -> elt.getId().equals(citeNote);
      Citation containsCiteNote = uncheckedCitations.stream()
          .filter(isQualified).findAny().orElse(null);
      if (containsCiteNote != null) {
        containsCiteNote.addContentCited(citedContent);
      } else {
        // If there are more than one external links:
        // Use the first archive one for link; the second one is the unaccessble original
        if (extLinks != null) {
          Element extLink = extLinks.first();
          if (extLink != null) {
            String link = extLink.attr("href");
            //System.out.println(link);
            uncheckedCitations.add(new Citation("Web", citeNote, citedContent, link));
          } else {
            uncheckedCitations.add(new Citation("Other", citeNote));
          }
        } else {
          uncheckedCitations.add(new Citation("Other", citeNote));
        }
      }
    }
    return uncheckedCitations;
  }

  public Set<Citation> parseForCitations() {
    Set<Citation> checkedCitations = new HashSet<>();
    Set<Citation> uncheckedCitations = parseForRawCitations();
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
    // TODO: check revision timestamp.
    return true;
  }

  private static void log(String msg, String... vals) {
    System.out.println(String.format(msg, vals));
  }

}
