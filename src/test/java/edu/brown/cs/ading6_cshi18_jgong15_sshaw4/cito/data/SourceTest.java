package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import org.junit.Test;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class SourceTest {

  @Test
  public void obtainLinksCorrectlyTest() {
    String html = "<html><body><p><a href = \"blah.com\">blah</a></p></body></html>";
    Source src = new WebSource("test.com", html, new GregorianCalendar());
    ArrayList<String> strs = new ArrayList<>();
    strs.add("blah.com");
    assertEquals(strs, src.getLinks());
  }

  @Test
  public void multipleLinksTest() {
    String html = "<html><body>"
        + "<a href = \"halllo.edu\">froofs</a>"
        + "<ul><li><a href = \"nested.txt\">Cozy</a></li></ul>";
    Source src = new WebSource("test.com", html, new GregorianCalendar());
    ArrayList<String> strs = new ArrayList<>();
    strs.add("halllo.edu");
    strs.add("nested.txt");
    assertEquals(strs, src.getLinks());
  }

  @Test
  public void noHrefTest() {
    String html = "<html><body><a>nope none</a></body></html>";
    Source src = new WebSource("test.com", html, new GregorianCalendar());
    assertTrue("links detected when there aren't any", src.getLinks().isEmpty());
  }

  @Test
  public void moreThanOneAttribute() {
    String html = "<html>"
        + "<body>"
        + "<p><a id=\"id\" href=\"blah.com\" class=\"foo\">blah</a></p>"
        + "</body>"
        + "</html>";
    Source src = new WebSource("test.com", html, new GregorianCalendar());
    ArrayList<String> strs = new ArrayList<>();
    strs.add("blah.com");
    assertEquals(strs, src.getLinks());
  }

}
