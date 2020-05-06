package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki.Citation;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.async.AsyncSourceQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CitationTest {
  Citation citationOne;
  @Before
  public void setUp() {
    citationOne = new Citation(
        Citation.WEB_TYPE,
        "cite_note-1",
        "hello",
        "Book.org");
  }

  @After
  public void tearDown() {
    citationOne = null;
  }

  @Test
  public void testAddContentCited() {
    citationOne.addContentCited(" world");
    assertEquals(citationOne.getCitedContent(), "hello world");
  }

  @Test
  public void testCheckLink() {
    AsyncSourceQuery sq = new AsyncSourceQuery(120);
    assertTrue(citationOne
        .checkLink("https://en.wikipedia.org/wiki/Murphy%27s_law", sq));
    assertFalse(citationOne
        .checkLink("https://web.archive.org/web/20080312052959/http://" +
            "listserv.linguistlist.org/cgi-bin/wa?A2=ind0710B&L=ADS-L&P" +
            "=R432&I=-3", sq));
  }

  @Test
  public void testWebSetUp() {
    citationOne.webSetUp(new ArrayList<>(List.of(
        "https://www.nytimes.com/1948/06/13/archives/thingness-of-things-" +
            "resistentialism-it-says-here-is-the-very-latest.html")));
    assertEquals(citationOne.getType(), Citation.WEB_TYPE);
    assertEquals(citationOne.getInitialWebSource().getURL(),
        "https://www.nytimes.com/1948/06/13/archives/thingness-" +
            "of-things-resistentialism-it-says-here-is-the-very-latest.html");
    assertFalse(citationOne.getHasCycles());
    assertEquals(citationOne.getNumberOfGeneratingSources(), 1);
  }

}
