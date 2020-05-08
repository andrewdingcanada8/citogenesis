package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.builders;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.CitoWorld;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki.Citation;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for Citation.
 */
public class CitationBuilder {
  private String type;
  private String id;
  private String citedContent;
  private String referenceText;
  private List<String> url = DEFAULT_URL;
  private Integer timeout = DEFAULT_TIME_OUT;
  private Integer depth = DEFAULT_DEPTH;
  private Double threshold = DEFAULT_THRESHOLD;

  private static final List<String> DEFAULT_URL = new ArrayList<>();
  private static final String DEFAULT_CITED_CONTENT = "";
  private static final String DEFAULT_REFERENCE_TEXT = "";
  private static final int DEFAULT_TIME_OUT = 60;
  private static final int DEFAULT_DEPTH = 5;
  private static final double DEFAULT_THRESHOLD = 0.200;

  /**
   * Skeleton builder for testing.
   * @param type type of citation
   * @param id id of citation.
   */
  public CitationBuilder(
      String type,
      String id) {
    this.type = type;
    this.id = id;
    this.citedContent = "";
    this.referenceText = "";
  }

  /**
   * Builder without the url.
   * @param type type of the citation
   * @param id id of the citation.
   * @param citedContent content of the citation
   * @param referenceText reference text of the citation
   */
  public CitationBuilder(
      String type,
      String id,
      String citedContent,
      String referenceText) {
    this.type = type;
    this.id = id;
    this.citedContent = citedContent;
    this.referenceText = referenceText;
  }

  /**
   * Setter for url.
   * @param links url of the citation external web page
   * @return this
   */
  public CitationBuilder setUrl(List<String> links) {
    this.url = links;
    return this;
  }

  /**
   * Setter for timeout.
   * @param time integer of timeout as seconds.
   * @return this
   */
  public CitationBuilder setTimeout(Integer time) {
    this.timeout = time;
    return this;
  }

  /**
   * Setter for the depth of the graph search.
   * @param d integer of the depth.
   * @return this
   */
  public CitationBuilder setDepth(Integer d) {
    this.depth = d;
    return this;
  }

  /**
   * Setter for the threshold of graph search.
   * @param thresh threshold between 0 and 1.
   * @return this
   */
  public CitationBuilder setThreshold(Double thresh) {
    this.threshold = thresh;
    return this;
  }

  /**
   * Builder to build the citation.
   * @return a citation.
   */
  public Citation build() {
    if (type.equals(Citation.OTHER_TYPE)) {
      return new Citation(type, id, citedContent, referenceText);
    } else if (type.equals(Citation.WEB_TYPE)
        || type.equals(Citation.NON_HTML_TYPE)
        || type.equals(Citation.TIME_OUT_TYPE)
        || type.equals(Citation.SELF_TYPE)) {
      Citation citation = new Citation(type, id, citedContent, referenceText);
      citation.setTimeout(timeout);
      citation.setDepth(depth);
      citation.setThreshold(threshold);
      citation.webSetUp(url);
      return citation;
    } else {
      System.out.println("ERROR: Not a accepted citation type.");
      return null;
    }
  }
}
