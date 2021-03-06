package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters.source.CosSimThreshold;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.ops.CosSim;
import org.junit.Test;

import static org.junit.Assert.*;

public class CosineSimilarityTest {

  static final double THRESHOLD = 0.00001;

  @Test
  public void sameDocScoresOneTest() {
    String doc = "this is a basic document";
    assertEquals(new CosSim().apply(doc, doc), 1.0, THRESHOLD);
  }

  @Test
  public void diffDocScoresZeroTest() {
    String doc1 = "not anything to do with the other";
    String doc2 = "gross toes";
    assertEquals(new CosSim().apply(doc1, doc2), 0.0, THRESHOLD);
  }

  @Test
  public void someWhereInTheMiddleTest() {
    String doc1 = "doc words present dog blog";
    String doc2 = "words here but not the same";
    String doc3 = "doc words present dog";

    assertTrue(new CosSim().apply(doc1, doc2)
        < new CosSim().apply(doc1, doc3));

    // symmetric relations still hold
    assertEquals(new CosSim().apply(doc1, doc2),
        new CosSim().apply(doc2, doc1), THRESHOLD);
    assertEquals(new CosSim().apply(doc2, doc3),
        new CosSim().apply(doc3, doc2), THRESHOLD);
  }

  @Test
  public void removesStopWords() {
    String doc1 = "in the to that food the the the the the";
    String doc2 = "that that that that that to food the in in";
    assertEquals(new CosSim().apply(doc1, doc2), 1, THRESHOLD);
  }

  @Test
  public void caseInsensitive() {
    String doc1 = "all case";
    String doc2 = "ALL CASE";
    assertEquals(new CosSim().apply(doc1, doc2), 1, THRESHOLD);
  }
}
