package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.ops;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.components.GeneratingVertexFinder;

import java.util.Comparator;

public class GeneratingSourceFinder implements GeneratingVertexFinder<Source, String> {
  @Override
  public Comparator<Source> getCompr() {
    return Comparator.comparing(Source::getTimestamp);
  }
}
