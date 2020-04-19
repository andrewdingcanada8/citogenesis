package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HTMLQueryTest {

  HTMLQuery _query;
  HTMLQuery _timedQuery;

  @Before
  public void setUp() {
    _query = new HTMLQuery();
    _timedQuery = new HTMLQuery(3);
  }

  @After
  public void tearDown() {
    _query = null;
  }

  @Test
  public void canGetGoogleTest() throws QueryException {
    String googleHtml = _query.query("https://www.google.com/index.html");
    assertTrue("doesn't contain doctype", googleHtml.contains("<!doctype html>"));
    assertTrue("doesn't contain word google", googleHtml.contains("google"));
  }

  @Test
  public void dogPicThrowsExceptionTest() {
    assertThrows(QueryException.class,
        () -> _query.query("http://pngimg.com/uploads/dog/dog_PNG50414.png"));
  }

  @Test
  public void throwsOnTimeoutTest() {
    assertThrows(QueryException.class,
        () -> _timedQuery.query("http://httpstat.us/200?sleep=4000"));
  }

}
