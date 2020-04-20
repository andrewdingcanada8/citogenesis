package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class TimeStampQueryTest {

  Query<String, Calendar> _query;

  @Before
  public void setUp() {
    _query = new TimeStampQuery(5);
  }

  @After
  public void tearDown() {
    _query = null;
  }

  @Test
  public void archiveDotOrgTest() throws QueryException {
    assertEquals(_query.query("http://archive.org"),
        new GregorianCalendar(1997, 01, 26, 04, 58, 28));
  }

  @Test
  public void googleDotComTest() throws QueryException {
    assertEquals(_query.query("https://www.google.com"),
        new GregorianCalendar(1998, 11, 11, 18, 45, 51));
  }

  @Test
  public void urlDoesNotExist() throws QueryException {
    assertNull(_query.query("pleasedonotexistpleasepleaseplease"));
  }


}
