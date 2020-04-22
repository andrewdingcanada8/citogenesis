package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.WebTestUtils;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

public class SourceQueryTest {

  SourceQuery _query;

  @Before
  public void setUp() {
    _query = new SourceQuery();
    assumeTrue(WebTestUtils.checkURL("https://www.google.com"));
  }

  @After
  public void tearDown() {
    _query = null;
  }

  @Test
  public void googleTest() throws QueryException {
    Source google = _query.query("https://www.google.com");
    assertTrue(google.getLinks().stream().anyMatch(s -> s.contains("google")));

    Source google2 = _query.query("https://www.google.com");
    assertEquals(google, google2);
  }



}
