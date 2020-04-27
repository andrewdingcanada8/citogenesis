package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters.source;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.RootedSourcedMemGraph;

public interface SourceRule {
  boolean verify(Source src, Source prevSrc,
                 RootedSourcedMemGraph<Source, String> graph);
}


