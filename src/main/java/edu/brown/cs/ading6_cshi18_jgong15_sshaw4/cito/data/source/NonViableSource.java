package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.source;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;

import java.util.Calendar;
import java.util.List;

public final class NonViableSource implements Source {
  public static final NonViableSource INSTANCE;
  static {
    INSTANCE = new NonViableSource();
  }

  private NonViableSource() { }

  @Override
  public String title() {
    return "nonviable src";
  }

  @Override
  public String getHTML() {
    return null;
  }

  @Override
  public String getContent() {
    return null;
  }

  @Override
  public String getURL() {
    return null;
  }

  @Override
  public List<String> getLinks() {
    return null;
  }

  @Override
  public void queryTimestamp() {
  }

  @Override
  public Calendar getTimestamp() {
    return null;
  }
}
