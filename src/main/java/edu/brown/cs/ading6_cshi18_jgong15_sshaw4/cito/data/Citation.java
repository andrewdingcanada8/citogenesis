package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;

import java.util.Objects;
import java.util.Set;

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
  private WebSource initialWebSource;
  private Set<Vertex<Source, String>> genSources;
  private Set<Set<Vertex<Source, String>>> sccs;

  public Citation(String sourceType) {
    this.sourceType = sourceType;
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
