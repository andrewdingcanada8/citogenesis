package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.ops;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.components.GeneratingVertexFinder;

import java.util.Comparator;

public class GeneratingSourceFinder implements GeneratingVertexFinder<Source, String> {
  @Override
  public Comparator<Source> getCompr() {
    return (s, o) -> {
      // null object is always greater than
      if (s != null && o == null) {
        return -1;
      }
      if (s == null && o != null) {
        return 1;
      }
      return s.getTimestamp().compareTo(o.getTimestamp());
    };
  }
}
