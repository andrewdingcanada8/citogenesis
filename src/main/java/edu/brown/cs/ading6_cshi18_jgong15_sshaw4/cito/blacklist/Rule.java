package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.blacklist;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.RootedSourcedMemGraph;

public interface Rule {
  boolean verify(String url, Vertex<Source, String> prev,
                        RootedSourcedMemGraph<Source, String> graph);
}
