package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.sync;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.source.WebSource;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.sync.HTMLQuery;

import java.util.Calendar;

public class SourceQuery implements Query<String, Source> {
  private Query<String, String> htmlQuery;
  private Query<String, Calendar> timeQuery;

  public SourceQuery(int timeOutInSec) {
    htmlQuery = new HTMLQuery(timeOutInSec);
    timeQuery = new TimeStampQuery(timeOutInSec);
  }

  @Override
  public Source query(String url) throws QueryException {
    String html = htmlQuery.query(url);
    Calendar timestamp = timeQuery.query(url);
    return new WebSource(url, html);
  }
}
