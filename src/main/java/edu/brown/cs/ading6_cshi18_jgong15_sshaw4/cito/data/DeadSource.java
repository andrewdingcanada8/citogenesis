package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class DeadSource extends Source {
  @Override
  public String getHTML() {
    return "";
  }

  @Override
  public String getURL() {
    return "";
  }

  @Override
  public List<String> getLinks() {
    return Collections.emptyList();
  }

  @Override
  public Calendar getTimestamp() {
    throw new UnsupportedOperationException("dead sources have no timestamps");
  }


}
