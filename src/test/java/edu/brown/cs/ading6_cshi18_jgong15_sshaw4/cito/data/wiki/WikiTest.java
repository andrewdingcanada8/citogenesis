package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.WikiQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WikiTest {
  Wiki wikiMurphysLaw;
  Wiki wikiBrownUniversity;

  @Before
  public void setUp() throws QueryException {
    String urlMurphysLaw = "https://en.wikipedia.org/wiki/Murphy%27s_law";
    wikiMurphysLaw = new WikiQuery(30).query(urlMurphysLaw);

    String urlBrownUniversity = "https://en.wikipedia.org/wiki/Brown_University";
    wikiBrownUniversity = new WikiQuery(30).query(urlBrownUniversity);
  }

  @After
  public void tearDown() {
    wikiMurphysLaw = null;
  }

  @Test
  public void testGetCitationIDs() {
    assertEquals(wikiMurphysLaw.getCitationIDs().size(), 27);
    assertEquals(wikiBrownUniversity.getCitationIDs().size(), 151);
  }
}
