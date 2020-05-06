package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.parsers;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki.Citation;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.builders.CitationBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
  }

  /**
   * Getter for html.
   * @return html as string.
   */
  public String getHtml() {
    return html;
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
    // Parses out the superscript citations in text
    Elements refs = doc.select("sup.reference > a");
    for (Element ref : refs) {
      // Parses out the id used for the end of the wiki page reference
      String citeNote = ref.attr("href");
      Element prev = ref.parent().parent();
      String citedContent = prev.text();
      // Check if the citation is already made with another instance of cited content
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
  public String parseForTitle() {
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
      return content.outerHtml();
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
    if (citationIDToContent == null) {
      citationIDToContent = createCitationIDToContent(doc);
    }
    return citationIDToContent.keySet();
  }

  /**
   * Returns a citation from an id.
   *
   * @param citeNoteID String that is a citation id.
   * @param timeout timeout time out value in seconds
   * @param depth depth of the graph search
   * @param threshold threshold for graph search between 0 and 1
   * @return a citation corresponding to the id.
   */
  public Citation parserForCitationFromID(
      String citeNoteID, Integer timeout, Integer depth, Double threshold)  {
    if (citationIDToContent == null) {
      citationIDToContent = createCitationIDToContent(doc);
    }
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
      if (extLinks != null) {
        List<String> links = new ArrayList<>();
        for (Element extLink : extLinks) {
          if (extLink != null) {
            String link = extLink.attr("href");
            links.add(link);
          }
        }
        if (links.isEmpty()) {
          return new CitationBuilder(
              Citation.OTHER_TYPE, citeNoteID, citedContent, referenceText).build();
        } else {
          return new CitationBuilder(
              Citation.WEB_TYPE, citeNoteID, citedContent, referenceText)
              .setTimeout(timeout)
              .setDepth(depth)
              .setThreshold(threshold)
              .setUrl(links)
              .build();
        }
      } else {
        return new CitationBuilder(
            Citation.OTHER_TYPE, citeNoteID, citedContent, referenceText).build();
      }
    }
  }

  /**
   * Parse the wiki html for citations.
   *
   * @return a set of citation that hasn't been checked for revision date
   * @param timeout timeout time out value in seconds
   * @param depth depth of the graph search
   * @param threshold threshold for graph search between 0 and 1
   */
  public Set<Citation> parseForRawCitations(
      Integer timeout, Integer depth, Double threshold) {
    Set<Citation> uncheckedCitations = new HashSet<>();
    if (citationIDToContent == null) {
      citationIDToContent = createCitationIDToContent(doc);
    }
    Set<String> citationIDs = citationIDToContent.keySet();
    for (String citationID : citationIDs) {
      Citation newCitation = parserForCitationFromID(
          citationID, timeout, depth, threshold);
      if (newCitation != null) {
        uncheckedCitations.add(newCitation);
      }
    }
    return uncheckedCitations;
  }
}
