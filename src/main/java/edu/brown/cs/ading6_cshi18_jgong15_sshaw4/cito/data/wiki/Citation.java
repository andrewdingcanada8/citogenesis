package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.graph.AsyncQueryWebGraph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.ops.GeneratingSourceFinder;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.async.AsyncSourceQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.QueryCacher;
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
   * If the type is Web, it is a typical citation.
   * If the type is Self, the generating source IS the wikipedia page.
   * If the type is "Other", all other fields are null.
   */
  private String sourceType;

  private String citedContent;
  private Number contentWordCount;
  private Number numberOfGeneratingSources;
  private Source initialWebSource;
  private List<Vertex<Source, String>> genSources;
  private Set<Set<Vertex<Source, String>>> sccs;
  private Boolean hasCycles;

  private static final int TIME_OUT = 20;

  public Citation(String sourceType) {
    this.sourceType = sourceType;
  }

  public Citation(String sourceType, String citedContent, String url) {
    this.sourceType = sourceType;
    if (sourceType.equals("Self") || sourceType.equals("Other")) {
      numberOfGeneratingSources = 0;
      initialWebSource = null;
      genSources = null;
      sccs = null;
    } else {
      this.citedContent = citedContent;
      AsyncSourceQuery sq = new AsyncSourceQuery(TIME_OUT);
      try {
        Source src = sq.query(url).join();
        initialWebSource = src;
        AsyncQueryWebGraph nyGraph = new AsyncQueryWebGraph(
            src, new QueryCacher<>(sq, 500), citedContent, 2);
        nyGraph.load();
        Vertex<Source, String> hv = nyGraph.getHead();
        List<Set<Vertex<Source, String>>> comps = new Tarjan().search(hv);
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
        initialWebSource = null;
        hasCycles = false;
        genSources = new ArrayList<>();
        numberOfGeneratingSources = 0;
      }
    }
  }

  public List<Vertex<Source, String>> getGenSources() {
    return genSources;
  }

  public Number getNumberOfGeneratingSources() {
    return numberOfGeneratingSources;
  }

  public Boolean getHasCycles() {
    return hasCycles;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Citation citation = (Citation) o;
    return Objects.equals(sourceType, citation.sourceType)
        && Objects.equals(citedContent, citation.citedContent)
        && Objects.equals(numberOfGeneratingSources, citation.numberOfGeneratingSources)
        && Objects.equals(initialWebSource, citation.initialWebSource);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceType, citedContent, numberOfGeneratingSources, initialWebSource);
  }
}