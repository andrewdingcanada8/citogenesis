package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.WebTestUtils;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.async.AsyncSourceQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.sync.SourceQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

public class SourceQueryTest {

  SourceQuery _query;
  AsyncSourceQuery _asyncQuery;

  @Before
  public void setUp() {
    assumeTrue(WebTestUtils.checkURL("https://www.google.com"));
    _query = new SourceQuery(WebTestUtils.HTTP_TIMEOUT);
    _asyncQuery = new AsyncSourceQuery(WebTestUtils.HTTP_TIMEOUT);
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

  @Test
  public void aSyncGoogleTest() throws QueryException {
    CompletableFuture<Source> google = _asyncQuery.query("https://www.google.com");
    CompletableFuture<Source> google2 = _asyncQuery.query("https://www.google.com");

    CompletableFuture.allOf(google, google2).join();
    assertTrue(google.join().getLinks().stream().anyMatch(s -> s.contains("google")));
    assertEquals(google.join(), google2.join());
  }



}
