package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.WebTestUtils;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

public class WikiQueryTest {

  WikiQuery _query;

  @Before
  public void setUp() {
    assumeTrue(WebTestUtils.checkURL("https://en.wikipedia.org/wiki/Murphy%27s_law"));
    _query = new WikiQuery(WebTestUtils.HTTP_TIMEOUT);
  }

  @After
  public void tearDown() {
    _query = null;
  }

  @Test
  public void testQuery() throws QueryException, IOException {
    Source wikiMurphysLaw = _query.query("https://en.wikipedia.org/wiki/Murphy%27s_law");
    Document doc = Jsoup.parse(wikiMurphysLaw.getHTML());
    log(doc.title());
    assertEquals(doc.title(), "Murphy's law - Wikipedia");
  }

  private static void log(String msg, String... vals) {
    System.out.println(String.format(msg, vals));
  }

}
