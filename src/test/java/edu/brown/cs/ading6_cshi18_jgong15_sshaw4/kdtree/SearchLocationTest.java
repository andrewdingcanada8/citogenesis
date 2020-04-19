package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.kdtree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SearchLocationTest {
  /**
   * Tests that a SearchLocation can return its dimension.
   */
  @Test
  public void testDimension() {
    SearchLocation sl = new SearchLocation(1, 20.3, -4);
    assertEquals(3, sl.getDimension());
  }

  /**
   * Tests that a SearchLocation can return its coordinates.
   */
  @Test
  public void testCoordinates() throws DimensionException {
    SearchLocation sl = new SearchLocation(1, 20.3, -4);
    assertEquals(1, sl.getCoordinate(0), 0.00000001);
    assertEquals(20.3, sl.getCoordinate(1), 0.00000001);
    assertEquals(-4, sl.getCoordinate(2), 0.00000001);
    Throwable e = null;
    try {
      sl.getCoordinate(3);
    } catch (Exception ex) {
      e = ex;
    }
    assertTrue(e instanceof DimensionException);
  }

  /**
   * Tests that a SearchLocation can find the Euclidean distance to another
   * Locatable.
   */
  @Test
  public void testDistance() throws DimensionException {
    SearchLocation sl = new SearchLocation(1, 20.3, -4);
    Point p = new Point(15.68, 2, 2.7);
    assertEquals(24.3984097842, sl.distanceTo(p), 0.00000001);
  }

  /**
   * Tests that a SearchLocation can find the axis distance to another Locatable
   * on each of its axes.
   */
  @Test
  public void testAxisDistance() throws DimensionException {
    SearchLocation sl = new SearchLocation(1, 20.3, -4);
    Point p = new Point(15.68, 2, -4);
    assertEquals(14.68, sl.axisDistanceTo(p, 0), 0.00000001);
    assertEquals(18.3, sl.axisDistanceTo(p, 1), 0.00000001);
    assertEquals(0, sl.axisDistanceTo(p, 2), 0.00000001);
  }
}
