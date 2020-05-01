package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki.Citation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CitationTest {
  Citation citationOne;
  @Before
  public void setUp() {
    citationOne = new Citation(
        "Web",
        "cite_note-1",
        "hello",
        "Book.org");
  }

  @After
  public void tearDown() {
    citationOne = null;
  }

  @Test
  public void testAddContentCited() {
    citationOne.addContentCited(" world");
    assertEquals(citationOne.getCitedContent(), "hello world");
  }

}
