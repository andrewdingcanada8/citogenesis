package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters.url;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.RootedSourcedMemGraph;

public interface URLRule {
  boolean verify(String url, String prevUrl,
                        RootedSourcedMemGraph<Source, String> graph);
}
