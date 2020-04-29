package edu.brown.cs.ading6_cshi18_jgong15_sshaw4;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.sync.CheckConnectionQuery;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assume.*;

public final class WebTestUtils {

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

  // example code for CompletableFutures
  @Ignore
  @Test
  public void blah() {
    CompletableFuture<Integer> countFut = countToMillion();
    System.out.println("processing other stuff");
    System.out.println("more stuff");
    int count = countFut.join();
    System.out.println("count: " + count);
  }

  public CompletableFuture<Integer> countToMillion() {
    return CompletableFuture.supplyAsync(() -> {
      int count;
      for (count = 0; count < 1000000; count++) ;
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return count;

    });
  }
}
