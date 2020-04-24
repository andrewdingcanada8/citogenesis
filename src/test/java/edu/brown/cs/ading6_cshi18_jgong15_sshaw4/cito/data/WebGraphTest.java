package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.sync.SourceQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import org.junit.Test;

public class WebGraphTest {

  @Test
  public void SanityCheckTest() throws QueryException, GraphException {
    //assumeTrue(WebTestUtils.checkURL("https://www.nytimes.com/"));
    SourceQuery sq = new SourceQuery(5);
    Source src = sq.query("https://www.google.com");
    WebGraph nyGraph = new WebGraph(src, sq, 1);
    nyGraph.getHead();
  }


}
