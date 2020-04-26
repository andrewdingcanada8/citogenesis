package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import java.util.Calendar;
import java.util.List;

public interface Source {

  /**
   * Get webpage html.
   * @return HTML
   */
  String getHTML();

  /**
   * Get parsed content of the source.
   * @return content
   */
  String getContent();

  /**
   * Get webpage url.
   * @return URL
   */
  String getURL();

  /**
   * Return all hrefs.
   * @return hrefs
   */
  List<String> getLinks();

  /**
   * Get timestamp of the source.
   * @return timestamp
   */
  Calendar getTimestamp();
}
