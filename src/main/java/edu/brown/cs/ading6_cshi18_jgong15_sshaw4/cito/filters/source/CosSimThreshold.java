package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters.source;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.ops.CosSim;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.RootedSourcedMemGraph;

@Deprecated
public class CosSimThreshold implements SourceRule {

  private static final double THRESHOLD = 0.2;

  @Override
  public boolean verify(Source src,
                        Source prevSrc,
                        RootedSourcedMemGraph<Source, String> graph) {
    // lemmatize both documents and acquire lists of lemmas
    return new CosSim().apply(src.getContent(), prevSrc.getContent()) >= THRESHOLD;
  }
}
