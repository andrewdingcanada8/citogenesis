package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.source;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DeadSource implements Source {

  private String url;

  public DeadSource(String url) {
    this.url = url;
  }

  @Override
  public String title() {
    return "ded boi";
  }

  @Override
  public String getHTML() {
    return "";
  }

  @Override
  public String getContent() {
    return "";
  }

  @Override
  public String getURL() {
    return url;
  }

  @Override
  public List<String> getLinks() {
    return Collections.emptyList();
  }

  @Override
  public void queryTimestamp() {
  }

  @Override
  public Calendar getTimestamp() {
    throw new UnsupportedOperationException("dead sources have no timestamps");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeadSource that = (DeadSource) o;
    return Objects.equals(url, that.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url);
  }
}
