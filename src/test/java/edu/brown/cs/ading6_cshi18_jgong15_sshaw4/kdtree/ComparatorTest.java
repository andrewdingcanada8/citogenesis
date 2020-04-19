package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.kdtree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComparatorTest {
  /**
   * Tests that an DistanceComparator correctly compares distances to a target
   * Locatable.
   */
  @Test
  public void testDistanceComparator() {
    Point p1 = new Point(0, 0);
    Point p2 = new Point(1, 0);
    Point p3 = new Point(1, 1);
    DistanceComparator ca = new DistanceComparator(p1, true);
    DistanceComparator cd = new DistanceComparator(p1, false);
    assertEquals(-1, ca.compare(p2, p3));
    assertEquals(1, ca.compare(p3, p2));
    assertEquals(1, cd.compare(p2, p3));
    assertEquals(-1, cd.compare(p3, p2));
  }

  /**
   * Tests that an AxisComparator correctly compares coordinates on a specified
   * axis.
   */
  @Test
  public void testAxisComparator() {
    Point p1 = new Point(2, -4, 3);
    Point p2 = new Point(9, -6, 3);
    AxisComparator c1 = new AxisComparator(0);
    AxisComparator c2 = new AxisComparator(1);
    AxisComparator c3 = new AxisComparator(2);
    assertEquals(-1, c1.compare(p1, p2));
    assertEquals(1, c1.compare(p2, p1));
    assertEquals(1, c2.compare(p1, p2));
    assertEquals(-1, c2.compare(p2, p1));
    assertEquals(0, c3.compare(p1, p2));
    assertEquals(0, c3.compare(p2, p1));
  }
}
