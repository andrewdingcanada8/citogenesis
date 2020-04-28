package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import org.junit.Test;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class SourceTest {

  @Test
  public void obtainLinksCorrectlyTest() {
    String html = "<html><body><p><a href = \"http://blah.com\">blah</a></p></body></html>";
    Source src = new WebSource("test.com", html);
    ArrayList<String> strs = new ArrayList<>();
    strs.add("http://blah.com");
    assertEquals(strs, src.getLinks());
    assertEquals("blah", src.getContent());
  }

  @Test
  public void multipleLinksTest() {
    String html = "<html><body>"
        + "<p><a href = \"https://halllo.edu\">froofs</a></p>"
        + "<ul><li><a href = \"http://nested.txt\">Cozy</a></li></ul>";
    Source src = new WebSource("test.com", html);
    ArrayList<String> strs = new ArrayList<>();
    strs.add("https://halllo.edu");
    strs.add("http://nested.txt");
    assertEquals(strs, src.getLinks());
    assertEquals("froofs Cozy", src.getContent());
  }

  @Test
  public void notInTextElTest() {
    String html = "<html><body>"
        + "<a href = \"https://halllo.edu\">froofs</a>"
        + "<ul><li><a href = \"http://nested.txt\">Cozy</a></li></ul>";
    Source src = new WebSource("test.com", html);
    ArrayList<String> strs = new ArrayList<>();
    strs.add("http://nested.txt");
    assertEquals(strs, src.getLinks());
    assertEquals("Cozy", src.getContent());
  }

  @Test
  public void noHrefTest() {
    String html = "<html><body><a>nope none</a></body></html>";
    Source src = new WebSource("test.com", html);
    assertTrue("links detected when there aren't any", src.getLinks().isEmpty());
    assertEquals("", src.getContent());
  }

  @Test
  public void moreThanOneAttribute() {
    String html = "<html>"
        + "<body>"
        + "<p><a id=\"id\" href=\"/blah\" class=\"foo\">blah</a></p>"
        + "</body>"
        + "</html>";
    Source src = new WebSource("http://test.com", html);
    ArrayList<String> strs = new ArrayList<>();
    strs.add("http://test.com/blah");
    assertEquals(strs, src.getLinks());
    assertEquals("blah", src.getContent());
  }

  @Test
  public void extractsAllTextTagsTest() {
    String html = "<html>"
        + "<body>"
        + "<p>i'm a teapot</p>"
        + "<h1>short and stout</h1>"
        + "<h2>here is my handle</h2>"
        + "<h3>here is my spout</h3>"
        + "<h4>I kinda forget the rest</h4>"
        + "<h5>WOOOOP</h5>"
        + "<h6>st about tipping over</h6>"
        + "<li>i give up</li></body></html>";
    Source src = new WebSource("http://test.com", html);
    assertTrue(src.getContent().contains("i'm a teapot"));
    assertTrue(src.getContent().contains("short and stout"));
    assertTrue(src.getContent().contains("here is my handle"));
    assertTrue(src.getContent().contains("here is my spout"));
    assertTrue(src.getContent().contains("I kinda forget the rest"));
    assertTrue(src.getContent().contains("WOOOOP"));
    assertTrue(src.getContent().contains("st about tipping over"));
    assertTrue(src.getContent().contains("i give up"));
  }

}
