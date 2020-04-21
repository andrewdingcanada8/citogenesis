package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import java.util.Calendar;
import java.util.List;

public abstract class Source {

  private int depth;

  public Source() {
    this(-1);
  }
  public Source(int depth) {
    this.depth = depth;
  }

  /**
   * Get webpage html.
   * @return HTML
   */
  public abstract String getHTML();

  /**
   * Get webpage url.
   * @return URL
   */
  public abstract String getURL();

  /**
   * Return all hrefs.
   * @return hrefs
   */
  public abstract List<String> getLinks();

  /**
   * Get timestamp of the source.
   * @return timestamp
   */
  public abstract Calendar getTimestamp();
}
