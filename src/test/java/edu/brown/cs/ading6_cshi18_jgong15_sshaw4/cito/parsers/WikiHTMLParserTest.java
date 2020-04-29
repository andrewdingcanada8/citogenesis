package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.parsers;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.sync.TimeStampQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.sync.HTMLQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

public class WikiHTMLParserTest {
  private WikiHTMLParser _parser;
  private Query<String, String> htmlQuery = new HTMLQuery(30);
  private Query<String, Calendar> timeQuery = new TimeStampQuery(30);

  @Before
  public void setUp() throws QueryException {
    String url = "https://en.wikipedia.org/wiki/Murphy%27s_law";
    String html = htmlQuery.query(url);
    Calendar timestamp = timeQuery.query(url);
    _parser = new WikiHTMLParser(url, html, timestamp);
  }

  @After
  public void tearDown() {
    _parser = null;
  }

  @Test
  public void testParseForRawCitations() {
    _parser.parseForRawCitations();
    //assertEqual(_parser.parseForRawCitation(),);

  }


}
