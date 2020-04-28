package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import java.util.*;

public class DummySource implements Source {

  private String url;

  public DummySource(String url) {
    this.url = url;
  }

  @Override
  public String getHTML() {
    return "lorem ipsum";
  }

  @Override
  public String getContent() {
    return "loreum ipsum";
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
  public Calendar getTimestamp() {
    return new GregorianCalendar();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DummySource that = (DummySource) o;
    return Objects.equals(url, that.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url);
  }
}
