package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters.source.CosSimThreshold;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.ops.CosSim;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.async.AsyncSourceQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import org.junit.Assume.*;
import org.junit.Ignore;
import org.junit.Test;

public class CosineThresholdTest {

  @Ignore
  @Test
  public void someStatsCheckTest() throws QueryException {
    Source nySrc1 = new AsyncSourceQuery(5).query("https://www.nytimes.com/2020/04/26/business/coronavirus-small-business-loans-large-companies.html").join();
    Source nySrc2 = new AsyncSourceQuery(5).query("https://www.nytimes.com/2020/04/20/business/shake-shack-returning-loan-ppp-coronavirus.html").join();
    System.out.println("Should be similar: " + new CosSim().apply(nySrc1.getContent(), nySrc2.getContent()));

    Source nySrc3 = new AsyncSourceQuery(5).query("https://www.nytimes.com/2020/04/24/technology/zoom-rivals-virus-facebook-google.html").join();
    System.out.println("Somewhat similar: " + new CosSim().apply(nySrc1.getContent(), nySrc3.getContent()));


    Source medSrc = new AsyncSourceQuery(5).query("https://medium.com/@adriensieg/text-similarities-da019229c894").join();
    System.out.println("Not similar: " + new CosSim().apply(nySrc1.getContent(), medSrc.getContent()));

    Source petSrc = new AsyncSourceQuery(5).query("https://en.wikipedia.org/wiki/Pet_door").join();
    System.out.println("Not similar: " + new CosSim().apply(petSrc.getContent(), nySrc2.getContent()));

    Source desSrc = new AsyncSourceQuery(5).query("http://dianeburgiodesign.com/").join();
    System.out.println("Not similar: " + new CosSim().apply(desSrc.getContent(), nySrc2.getContent()));

    Source google1 = new AsyncSourceQuery(5).query("http://google.com").join();
    Source google2 = new AsyncSourceQuery(5).query("http://google.com").join();
    System.out.println("Google (server-balanced) comparison: " + new CosSim().apply(google1.getContent(), google2.getContent()));
  }

}
