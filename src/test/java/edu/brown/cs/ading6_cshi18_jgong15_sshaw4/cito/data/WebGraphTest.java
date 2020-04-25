package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.async.AsyncSourceQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.sync.SourceQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import org.junit.Test;

public class WebGraphTest {

  @Test
  public void sanityCheckTest() throws QueryException, GraphException {
    //assumeTrue(WebTestUtils.checkURL("https://www.nytimes.com/"));
    SourceQuery sq = new SourceQuery(1);
    Source src = sq.query("https://www.nytimes.com/2020/04/24/us/coronavirus-us-usa-updates.html");
    WebGraph nyGraph = new WebGraph(src, sq, 1);
    nyGraph.getHead();
  }

  @Test
  public void asyncSanityCheckTest() throws QueryException, GraphException {
    //assumeTrue(WebTestUtils.checkURL("https://www.nytimes.com/"));
    AsyncSourceQuery sq = new AsyncSourceQuery(1);
    Source src = sq.query("https://www.nytimes.com/2020/04/24/us/coronavirus-us-usa-updates.html").join();
    AsyncWebGraph nyGraph = new AsyncWebGraph(src, sq, 2);
    nyGraph.getHead();
  }


}
