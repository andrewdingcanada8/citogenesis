package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.WebTestUtils;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.async.AsyncHTMLQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.sync.HTMLQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

public class HTMLQueryTest {

  HTMLQuery _query;
  HTMLQuery _timedQuery;
  AsyncHTMLQuery _asyncQuery;
  AsyncHTMLQuery _timedAsyncQuery;

  @Before
  public void setUp() {
    assumeTrue(WebTestUtils.HTTP_TIMEOUT > 0); // needed since global website check isn't done here
    _query = new HTMLQuery(WebTestUtils.HTTP_TIMEOUT);
    _timedQuery = new HTMLQuery(3);
    _asyncQuery = new AsyncHTMLQuery(WebTestUtils.HTTP_TIMEOUT);
    _timedAsyncQuery = new AsyncHTMLQuery(2);
  }

  @After
  public void tearDown() {
    _query = null;
  }

  @Test
  public void canGetGoogleTest() throws QueryException, ExecutionException, InterruptedException {
    assumeTrue(WebTestUtils.checkURL("https://www.google.com"));
    String googleHtml = _query.query("https://www.google.com/index.html");
    assertTrue("doesn't contain doctype", googleHtml.contains("<!doctype html>"));
    assertTrue("doesn't contain word google", googleHtml.contains("google"));

    String googleHtml2 = _asyncQuery.query("https://www.google.com/index.html").get();
    assertTrue("doesn't contain doctype", googleHtml.contains("<!doctype html>"));
    assertTrue("doesn't contain word google", googleHtml.contains("google"));
  }

  @Test
  public void dogPicThrowsExceptionTest() {
    assumeTrue(WebTestUtils.checkURL("http://pngimg.com/uploads/dog/dog_PNG50414.png"));
    assertThrows(QueryException.class,
        () -> _query.query("http://pngimg.com/uploads/dog/dog_PNG50414.png"));

    assertThrows(ExecutionException.class,
        () -> _asyncQuery.query("http://pngimg.com/uploads/dog/dog_PNG50414.png").get());
  }

  @Test
  public void throwsOnTimeoutTest() {
    assumeTrue(WebTestUtils.checkURL("http://httpstat.us"));
    assertThrows(QueryException.class,
        () -> _timedQuery.query("http://httpstat.us/200?sleep=4000"));
    assertThrows(ExecutionException.class,
        () -> _timedAsyncQuery.query("http://httpstat.us/200?sleep=4000").get());
  }

}
