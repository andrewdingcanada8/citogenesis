package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.parsers;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki.Citation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.HashMap;
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
   * Document representation of the wiki page.
   */
  private Document doc;
  /**
   * Hashmap of citation ids to content.
   */
  private HashMap<String, String> citationIDToContent;

  /**
   * Constructor for parser with timestamp.
   *
   * @param url       url of the wiki page
   * @param html      html of the wiki
   * @param timestamp timestamp of the wiki page
   */
  public WikiHTMLParser(String url, String html, Calendar timestamp) {
    this.url = url;
    this.html = html;
    this.timestamp = timestamp;
    doc = Jsoup.parse(html, url);
    citationIDToContent = createCitationIDToContent(doc);
  }

  /**
   * Constructor for parser.
   *
   * @param url  url of the wiki page.
   * @param html html of the wiki
   */
  public WikiHTMLParser(String url, String html) {
    this.url = url;
    this.html = html;
    this.timestamp = null;
    doc = Jsoup.parse(html, url);
    citationIDToContent = createCitationIDToContent(doc);
  }

  /**
   * Helper function for the constructor to create a hashmap of
   * citation id to content.
   *
   * @param doc doc from the parsing.
   * @return a hashmap of citation id to content.
   */
  public static HashMap<String, String> createCitationIDToContent(Document doc) {
    HashMap<String, String> citationIDToContent = new HashMap<>();
    Elements refs = doc.select("sup.reference > a");
    for (Element ref : refs) {
      String citeNote = ref.attr("href");
      Element prev = ref.parent().parent();
      String citedContent = prev.text();
      if (citationIDToContent.get(citeNote) != null) {
        String citedContentPart = citationIDToContent.get(citeNote);
        String updatedCitedContent;
        if (!citedContent.equals(citedContentPart)) {
          updatedCitedContent = citedContent + citedContentPart;
        } else {
          updatedCitedContent = citedContent;
        }
        citationIDToContent.put(citeNote, updatedCitedContent);
      } else {
        citationIDToContent.put(citeNote, citedContent);
      }
    }
    return citationIDToContent;
  }

  /**
   * Parse for title of wiki page.
   *
   * @return title of the wiki page.
   */
  public String parserForTitle() {
    return doc.getElementsByTag("title").text();
  }

  /**
   * Parse for and return the text content of the wiki page.
   *
   * @return String of text content.
   */
  public String parseForContent() {
    Elements content = doc.select("#content");
    if (content != null) {
      return content.text();
    } else {
      return "";
    }
  }

  /**
   * Parse for and return the html of the body-content.
   *
   * @return html
   */
  public String parseForContentHTML() {
    Elements content = doc.select("#content");
    if (content != null) {
      return content.html();
    } else {
      return "";
    }
  }

  /**
   * Parse the wiki citation ids.
   *
   * @return a set of strings representing the citation ids
   * For example, "cite_note-1"
   */
  public Set<String> parseForCitationIDs() {
    return citationIDToContent.keySet();
  }

  /**
   * Returns a citation from an id.
   *
   * @param citeNoteID String that is a citation id.
   * @return a citation corresponding to the id.
   */
  public Citation parserForCitationFromID(String citeNoteID)  {
    String citedContent = citationIDToContent.get(citeNoteID);
    if (citedContent == null) {
      return null;
    } else {
      Elements refContent = doc.select(citeNoteID);
      String referenceText = refContent.select(".reference-text").text();
      Elements extLinks = refContent.select(".external");
      Element timeAccessed = refContent.select(".reference-accessdate").first();
      // If there are more than one external links:
      // Use the first archive one for link; the second one is the unaccessble original
      System.out.println(extLinks);
      if (extLinks != null) {
        Element extLink = extLinks.first();
        System.out.println(extLink);
        if (extLink != null) {
          String link = extLink.attr("href");
          //System.out.println(link);
          return new Citation(
              "Web",
              citeNoteID,
              citedContent,
              referenceText,
              link);
        } else {
          return new Citation(
              "Other",
              citeNoteID,
              citedContent,
              referenceText);
        }
      } else {
        return new Citation(
            "Other",
            citeNoteID,
            citedContent,
            referenceText);
      }
    }
  }

  /**
   * Parse the wiki html for citations.
   *
   * @return a set of citation that hasn't been checked for revision date
   */
  public Set<Citation> parseForRawCitations() {
    Set<Citation> uncheckedCitations = new HashSet<>();
    // Parses out the superscript citations in text
    Elements refs = doc.select("sup.reference > a");
    for (Element ref : refs) {
      // Parses out the id used for the end of the wiki page reference
      String citeNote = ref.attr("href");
      Element prev = ref.parent().parent();
      String citedContent = prev.text();

      Elements refContent = doc.select(citeNote);
      String referenceText = refContent.select(".reference-text").text();
      System.out.println(referenceText);
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
            uncheckedCitations.add(new Citation(
                "Web",
                citeNote,
                citedContent,
                referenceText,
                link));
          } else {
            uncheckedCitations.add(new Citation(
                "Other",
                citeNote,
                citedContent,
                referenceText));
          }
        } else {
          uncheckedCitations.add(new Citation(
              "Other",
              citeNote,
              citedContent,
              referenceText));
        }
      }
    }
    return uncheckedCitations;
  }

  /**
   * Parse the wiki html for citations.
   *
   * @return a set of citation that has been checked for revision date
   */
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
   *
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
