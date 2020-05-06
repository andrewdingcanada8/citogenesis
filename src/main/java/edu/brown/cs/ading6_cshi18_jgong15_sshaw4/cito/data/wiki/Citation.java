package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.graph.AsyncSearchWebGraph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.source.DeadSource;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.ops.GeneratingSourceFinder;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.async.AsyncSourceQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.QueryCacher;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Graph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.segment.Tarjan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

/**
 * Class to represent a citation.
 */
public class Citation {
  /**
   * Type of the citation.
   * One of the following: Web, Self, Other
   * If the type is "Web", it is a typical citation.
   * If the type is "Time Out", the url timed out.
   * If the type is "Non-HTML", the algorithm cannot work on a non html page.
   * If the type is "Self", the generating source IS the wikipedia page.
   * If the type is "Other", all other fields are null.
   */
  private String type;
  /**
   * Id of a citation from the wikipedia html.
   * Has the generally the form "#cite_note-1" etc.
   */
  private String id;
  /**
   * Content text surrounding the citation.
   */
  private String citedContent;
  /**
   * Text of the citation at the bottom of the wikipedia page.
   */
  private String referenceText;
  /**
   * Number of generating sources.
   */
  private Number numberOfGeneratingSources;
  /**
   * The initial web source of the citation.
   * Null if the type is "Self" or "Other".
   * DeadSource if the type is "Time Out" or "Non-HTML"
   * WebSource if the type is "Web"
   */
  private Source initialWebSource;
  /**
   * List of generating sources.
   */
  private List<Vertex<Source, String>> genSources = new ArrayList<>();
  /**
   * Strongly connected component of the graph.
   */
  private List<Set<Vertex<Source, String>>> sccs;
  /**
   * Graph of the citation.
   */
  private Graph<Source, String> graph;
  /**
   * If the graph has cycles.
   */
  private Boolean hasCycles;
  /**
   * Time out value for the AsyncSourceQuery.
   */
  private Integer timeout = DEFAULT_TIME_OUT;
  /**
   * Depth value for the AsyncSearchWebGraph.
   */
  private Integer depth = DEFAULT_DEPTH;
  /**
   * Threshold for the AsyncSearchWebGraph.
   */
  private Double threshold = DEFAULT_THRESHOLD;

  public static final String WEB_TYPE = "Web";
  public static final String TIME_OUT_TYPE = "Time Out";
  public static final String NON_HTML_TYPE = "Non-HTML";
  public static final String SELF_TYPE = "Self";
  public static final String OTHER_TYPE = "Other";

  private static final int DEFAULT_TIME_OUT = 60;
  private static final int DEFAULT_DEPTH = 5;
  private static final double DEFAULT_THRESHOLD = 0.200;

  /**
   * Basic Constructor with type and id.
   * Mainly for tests.
   * @param type type of citation.
   * @param id id of citation.
   */
  public Citation(String type, String id) {
    this.type = type;
    this.id = id;
    this.citedContent = "";
    this.referenceText = "";
    if (type.equals(SELF_TYPE) || type.equals(OTHER_TYPE)) {
      numberOfGeneratingSources = 1;
      initialWebSource = null;
      hasCycles = false;
      genSources = new ArrayList<>();
      sccs = null;
      graph = null;
    }
  }

  /**
   * Constructor with citedContent and referenceText.
   * Values parsed from WikiHTMLParser.
   * @param type type of citation
   * @param id id of citation
   * @param citedContent content that the citation annotates.
   * @param referenceText text of reference at the bottom of wikipedia.
   */
  public Citation(
      String type,
      String id,
      String citedContent,
      String referenceText) {
    this.type = type;
    this.id = id;
    this.citedContent = citedContent;
    this.referenceText = referenceText;
    if (type.equals(SELF_TYPE) || type.equals(OTHER_TYPE)) {
      numberOfGeneratingSources = 1;
      initialWebSource = null;
      hasCycles = false;
      genSources = new ArrayList<>();
      sccs = null;
      graph = null;
    }
  }

  /**
   * Set up for a WEB-BASED citation.
   * Used in the Citation Builder to build web-based citation.
   * @param links list of urls
   */
  public void webSetUp(List<String> links) {
    // Find a link that doesn't time out or non html
    // If all time out or is non html, choose the first one.
    String url = links.get(0);
    AsyncSourceQuery sq = new AsyncSourceQuery(timeout);
    for (String link : links) {
      // Check if link is a dead source
      if (checkLink(link, sq)) {
        // Set url to the link that is not a dead source
        url = link;
        break;
      }
    }
    try {
      Source src = sq.query(url).join();
      if (src instanceof DeadSource) {
        if (src.title().equals(AsyncSourceQuery.TIMEOUT_ERROR)) {
          this.type = TIME_OUT_TYPE;
        } else if (src.title().equals(AsyncSourceQuery.NOT_HTML_ERROR)) {
          this.type = NON_HTML_TYPE;
        }
      } else {
        this.type = WEB_TYPE;
      }
      initialWebSource = src;
      AsyncSearchWebGraph nyGraph = new AsyncSearchWebGraph(
          src, new QueryCacher<>(sq, 500), citedContent,
          depth, threshold);
      graph = nyGraph;
      nyGraph.load();
      Vertex<Source, String> hv = nyGraph.getHead();
      List<Set<Vertex<Source, String>>> comps = new Tarjan().search(hv);
      sccs = comps;
      comps.stream().flatMap(Collection::stream).forEach(v -> v.getVal().queryTimestamp());
      hasCycles = comps.stream().anyMatch(c -> c.size() > 1);
      List<Vertex<Source, String>> gens = comps.stream()
          .map(comp -> {
            try {
              return new GeneratingSourceFinder().search(comp);
            } catch (GraphException e) {
              System.out.println("Error while finding gen sources: " + e.getMessage());
              return null;
            }
          })
          .collect(Collectors.toList());
      if (gens == null) {
        genSources = new ArrayList<>();
      } else {
        genSources = gens;
      }
      numberOfGeneratingSources = genSources.stream().map(source -> {
        if (source == null) {
          return 0;
        } else {
          return 1;
        }
      }).reduce(0, Integer::sum);
    } catch (QueryException | GraphException | CompletionException e) {
      System.out.println("ERROR: " + e.getMessage());
    }
  }

  /**
   * Check if the link is not a deadsource.
   * @param link url
   * @param sq query class
   * @return true if the link is not a deadsource.
   */
  public boolean checkLink(String link, AsyncSourceQuery sq) {
    boolean b = true;
    try {
      if (sq.query(link).join() instanceof DeadSource) {
        b = false;
      }
    } catch (QueryException | IllegalArgumentException e) {
      System.out.println(e.getMessage());
      b = false;
    }
    return b;
  }

  /**
   * Setter for timeout value.
   * @param timeout timeout in seconds.
   */
  public void setTimeout(Integer timeout) {
    this.timeout = timeout;
  }

  /**
   * Setter for depth value.
   * @param depth depth integer
   */
  public void setDepth(Integer depth) {
    this.depth = depth;
  }

  /**
   * Setter for threshold of async search web.
   * @param threshold double between 0 and 1.
   */
  public void setThreshold(Double threshold) {
    this.threshold = threshold;
  }

  /**
   * Returns the id of the Citation. E.g. "cite_note-1"
   * @return id as string
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the graph of the web associated with the citation.
   * Null if the type is "Other" or "Self".
   * @return the nygraph from AsyncWebGraph.
   */
  public Graph<Source, String> getGraph() {
    return graph;
  }

  /**
   * Returns the list of generating sources.
   * @return list of generating sources.
   */
  public List<Vertex<Source, String>> getGenSources() {
    return genSources;
  }

  /**
   * Returns the text of the reference object in wikipedia.
   * @return text of the reference.
   */
  public String getReferenceText() {
    return referenceText;
  }

  /**
   * Returns the sccs.
   * @return the sccs.
   */
  public List<Set<Vertex<Source, String>>> getSccs() {
    return sccs;
  }

  /**
   * Returns the number of generating sources.
   * 0 if the type is "Other" or "Self".
   * @return number of generating sources.
   */
  public Number getNumberOfGeneratingSources() {
    return numberOfGeneratingSources;
  }

  /**
   * Returns true if there's circular citations.
   * False if there's no circular citations OR
   * False if the type is "Other" or "Self".
   * @return boolean if there circular citations.
   */
  public Boolean getHasCycles() {
    return hasCycles;
  }

  /**
   * Returns the cited content.
   * @return the cited content as string.
   */
  public String getCitedContent() {
    return citedContent;
  }

  /**
   * Returns the initial web source.
   * @return the initial web source as a Source.
   */
  public Source getInitialWebSource() {
    return initialWebSource;
  }

  /**
   * Returns the type of the citation.
   * One of the following: Web, Self, Other
   * If the type is "Web", it is a typical citation.
   * If the type is "Time Out", the url timed out.
   * If the type is "Non-HTML", the algorithm cannot work on a non html page.
   * If the type is "Self", the generating source IS the wikipedia page.
   * If the type is "Other", all other fields are null.
   * @return type of the citation as a string.
   */
  public String getType() {
    return type;
  }

  /**
   * Append additional content cited to the citation.
   * @param content additional content.
   */
  public void addContentCited(String content) {
    citedContent += content;
  }

  /**
   * Equals method.
   * @param o object
   * @return true if equal
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Citation citation = (Citation) o;
    return type.equals(citation.type)
        && id.equals(citation.id)
        && Objects.equals(initialWebSource, citation.initialWebSource);
  }

  /**
   * Hashcode method.
   * @return int
   */
  @Override
  public int hashCode() {
    return Objects.hash(type, id, initialWebSource);
  }

  /**
   * To string method.
   * @return string.
   */
  @Override
  public String toString() {
    return "Citation{"
        +
        "type='" + type + '\''
        +
        ", id='" + id + '\''
        +
        ", referenceText='" + referenceText + '\''
        +
        ", numberOfGeneratingSources=" + numberOfGeneratingSources
        +
        ", initialWebSource=" + initialWebSource
        +
        ", genSources=" + genSources
        +
        '}';
  }
}
