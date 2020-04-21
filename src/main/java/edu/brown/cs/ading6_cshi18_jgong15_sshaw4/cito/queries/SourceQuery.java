package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.WebSource;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.HTMLQuery;

import java.util.Calendar;

public class SourceQuery implements Query<String, Source> {
  private Query<String, String> htmlQuery;
  private Query<String, Calendar> timeQuery;
  public SourceQuery() {
    htmlQuery = new HTMLQuery();
    timeQuery = new TimeStampQuery();
  }
  @Override
  public Source query(String url) throws QueryException {
    String html = htmlQuery.query(url);
    Calendar timestamp = timeQuery.query(url);
    return new WebSource(url, html, timestamp);
  }
}
