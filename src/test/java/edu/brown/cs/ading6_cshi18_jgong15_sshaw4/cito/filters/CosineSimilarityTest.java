package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters.source.CosSimThreshold;
import org.junit.Test;

import static org.junit.Assert.*;

public class CosineSimilarityTest {

  static final double THRESHOLD = 0.00001;

  @Test
  public void sameDocScoresOneTest() {
    String doc = "this is a basic document";
    assertEquals(CosSimThreshold.cosSim(doc, doc), 1.0, THRESHOLD);
  }

  @Test
  public void diffDocScoresZeroTest() {
    String doc1 = "not anything to do with the other";
    String doc2 = "gross toes";
    assertEquals(CosSimThreshold.cosSim(doc1, doc2), 0.0, THRESHOLD);
  }

  @Test
  public void someWhereInTheMiddleTest() {
    String doc1 = "this is a document with some words";
    String doc2 = "some words in here";
    String doc3 = "this is a document with a sentence";

    assertTrue(CosSimThreshold.cosSim(doc1, doc2)
        < CosSimThreshold.cosSim(doc1, doc3));
    assertEquals(CosSimThreshold.cosSim(doc1, doc2),
        CosSimThreshold.cosSim(doc2, doc1), THRESHOLD);
    assertEquals(CosSimThreshold.cosSim(doc2, doc3),
        CosSimThreshold.cosSim(doc3, doc2), THRESHOLD);
  }
}
