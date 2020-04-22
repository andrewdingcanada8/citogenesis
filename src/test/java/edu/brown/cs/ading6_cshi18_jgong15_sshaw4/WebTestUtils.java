package edu.brown.cs.ading6_cshi18_jgong15_sshaw4;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.CheckConnectionQuery;
import static org.junit.Assume.*;

public final class WebTestUtils {

  private WebTestUtils() { }

  public static int HTTP_TIMEOUT;

  static {
    try {
      HTTP_TIMEOUT = Integer.parseInt(System.getProperty("timeout"));
    } catch (NumberFormatException e) {
      HTTP_TIMEOUT = -1;
    }
  }

  public static boolean checkURL(String url) {
    assumeTrue(HTTP_TIMEOUT > 0);
    try {
      return new CheckConnectionQuery(HTTP_TIMEOUT).query(url);
    } catch (QueryException e) {
      return false;
    }
  }
}
