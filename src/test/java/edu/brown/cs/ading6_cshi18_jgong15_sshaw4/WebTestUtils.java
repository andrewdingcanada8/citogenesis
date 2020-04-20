package edu.brown.cs.ading6_cshi18_jgong15_sshaw4;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.CheckConnectionQuery;

public final class WebTestUtils {

  private WebTestUtils() { }

  public static boolean checkURL(String url) {
    try {
      return new CheckConnectionQuery(5).query(url);
    } catch (QueryException e) {
      return false;
    }
  }
}
