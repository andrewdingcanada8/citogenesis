package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.graph.AsyncQueryWebGraph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.ops.GeneratingSourceFinder;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.async.AsyncSourceQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.QueryCacher;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Graph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.segment.Tarjan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Citation {
  /**
   * Type of the source.
   * One of the following: Web, Self, Other
   * If the type is "Web", it is a typical citation.
   * If the type is "Self", the generating source IS the wikipedia page.
   * If the type is "Other", all other fields are null.
   */
  private String sourceType;
  private String id;
  private String citedContent;
  private String referenceText;
  private Number contentWordCount;
  private Number numberOfGeneratingSources;
  private Source initialWebSource;
  private List<Vertex<Source, String>> genSources;
  private List<Set<Vertex<Source, String>>> sccs;
  private Graph<Source, String> graph;
  private Boolean hasCycles;

  private static final int TIME_OUT = 20;

  public Citation(String sourceType, String id) {
    this.sourceType = sourceType;
    this.id = id;
    this.citedContent = "";
    if (sourceType.equals("Self") || sourceType.equals("Other")) {
      numberOfGeneratingSources = 0;
      initialWebSource = null;
      hasCycles = false;
      genSources = new ArrayList<>();
      sccs = new ArrayList<>();
      graph = null;
    }
  }

  public Citation(
      String sourceType,
      String id,
      String citedContent,
      String referenceText) {
    this.sourceType = sourceType;
    this.id = id;
    this.citedContent = citedContent;
    this.referenceText = referenceText;
    if (sourceType.equals("Self") || sourceType.equals("Other")) {
      numberOfGeneratingSources = 1;
      initialWebSource = null;
      hasCycles = false;
      genSources = new ArrayList<>();
      sccs = new ArrayList<>();
      graph = null;
    }
  }

  public Citation(
      String sourceType,
      String id,
      String citedContent,
      String referenceText,
      String url) {
    this.sourceType = sourceType;
    this.id = id;
    this.citedContent = citedContent;
    this.referenceText = referenceText;
    if (sourceType.equals("Self") || sourceType.equals("Other")) {
      numberOfGeneratingSources = 1;
      initialWebSource = null;
      hasCycles = false;
      genSources = new ArrayList<>();
      sccs = new ArrayList<>();
      graph = null;
    } else {
      AsyncSourceQuery sq = new AsyncSourceQuery(TIME_OUT);
      try {
        Source src = sq.query(url).join();
        initialWebSource = src;
        System.out.println("Citation source: " + src);
        AsyncQueryWebGraph nyGraph = new AsyncQueryWebGraph(
            src, new QueryCacher<>(sq, 500), citedContent, 2);
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
        genSources = gens;
        numberOfGeneratingSources = genSources.stream().map(source -> {
          if (source == null) {
            return 1;
          } else {
            return 0;
          }
        }).reduce(0, Integer::sum);
      } catch (Exception e) {
        numberOfGeneratingSources = 1;
        initialWebSource = null;
        hasCycles = false;
        genSources = new ArrayList<>();
        sccs = new ArrayList<>();
        graph = null;
      }
    }
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
   * Null if the sourceType is "Other" or "Self".
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
   * Word count of the content cited.
   * @return word count of the content cited.
   */
  public Number getContentWordCount() {
    return contentWordCount;
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
   * 0 if the sourceType is "Other" or "Self".
   * @return number of generating sources.
   */
  public Number getNumberOfGeneratingSources() {
    return numberOfGeneratingSources;
  }

  /**
   * Returns true if there's circular citations.
   * False if there's no circular citations OR
   * False if the sourceType is "Other" or "Self".
   * @return boolean if there circular citations.
   */
  public Boolean getHasCycles() {
    return hasCycles;
  }

  public String getCitedContent() {
    return citedContent;
  }

  public Source getInitialWebSource() {
    return initialWebSource;
  }

  public String getSourceType() {
    return sourceType;
  }

  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
    if (sourceType.equals("Self")) {
      numberOfGeneratingSources = 0;
      initialWebSource = null;
      genSources = null;
      sccs = null;
    }
  }

  public void addContentCited(String content) {
    citedContent += content;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Citation citation = (Citation) o;
    return sourceType.equals(citation.sourceType)
        && id.equals(citation.id)
        && Objects.equals(initialWebSource, citation.initialWebSource);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceType, id, initialWebSource);
  }
}
