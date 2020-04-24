package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.WebTestUtils;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.async.AsyncTimeStampQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.sync.TimeStampQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

public class TimeStampQueryTest {

  TimeStampQuery _query;
  AsyncTimeStampQuery _asyncQuery;

  @Before
  public void setUp() {
    assumeTrue(WebTestUtils.checkURL("http://archive.org"));
    _query = new TimeStampQuery(WebTestUtils.HTTP_TIMEOUT);
    _asyncQuery = new AsyncTimeStampQuery(WebTestUtils.HTTP_TIMEOUT);
  }

  @After
  public void tearDown() {
    _query = null;
  }

  @Test
  public void archiveDotOrgTest() throws QueryException, ExecutionException, InterruptedException {
    assertEquals(_query.query("http://archive.org"),
        new GregorianCalendar(1997, 01, 26, 04, 58, 28));
    assertEquals(_asyncQuery.query("http://archive.org").get(),
        new GregorianCalendar(1997, 01, 26, 04, 58, 28));
  }

  @Test
  public void googleDotComTest() throws QueryException, ExecutionException, InterruptedException {
    assertEquals(_query.query("https://www.google.com"),
        new GregorianCalendar(1998, 11, 11, 18, 45, 51));
    assertEquals(_asyncQuery.query("https://www.google.com").get(),
        new GregorianCalendar(1998, 11, 11, 18, 45, 51));
  }

  @Test
  public void urlDoesNotExist() throws QueryException, ExecutionException, InterruptedException {
    assertNull(_query.query("pleasedonotexistpleasepleaseplease"));
    assertNull(_asyncQuery.query("pleasedonotexistpleasepleaseplease").get());
  }


}
