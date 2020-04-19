package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.kdtree;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KDTreeTest {
  private KDTree<Point> t;
  private List<Point> points;
  private final int dimension = 4;
  private final int numPoints = 10000;
  private final double randomLimit = 100;

  @Before
  public void setUp() throws KDTreeException, DimensionException {
    points = new ArrayList<>();
    t = new KDTree<Point>(dimension);

    for (int pt = 0; pt < numPoints; pt++) {
      points.add(new Point(randomCoords()));
    }

    t.build(points);
  }

  private double[] randomCoords() {
    Random generator = new Random();
    double[] coordinates = new double[dimension];
    for (int axis = 0; axis < dimension; axis++) {
      coordinates[axis] = (generator.nextDouble() * 2 - 1) * randomLimit;
    }
    return coordinates;
  }

  private void sortBy(Locatable target) {
    DistanceComparator c = new DistanceComparator(target, true);
    Collections.sort(points, c);
  }

  private List<Point> listNeighbors(int k) {
    if (k <= numPoints) {
      return points.subList(0, k);
    } else {
      return points.subList(0, numPoints);
    }
  }

  private List<Point> listRadius(double r, Locatable target)
      throws DimensionException {
    int i = 0;
    while (i < points.size() && target.distanceTo(points.get(i)) <= r) {
      i++;
    }
    return points.subList(0, i);
  }

  @After
  public void tearDown() {
    points = null;
    t = null;
  }

  /**
   * Tests putting one element into the KDTree (left and right subtrees are both
   * null).
   *
   * @throws KDTreeException
   * @throws DimensionException
   */
  @Test
  public void testAddOne() throws KDTreeException, DimensionException {
    t = new KDTree<>(2);
    List<Point> toAdd = Arrays.asList(new Point(0, 0));
    t.build(toAdd);
    String expected = "[0] 0.0 0.0\n" + "   []\n" + "   []";
    String actual = t.treeDiagram();
    assertEquals(expected, actual);
    tearDown();
  }

  /**
   * Tests putting two elements into the KDTree (one subtree is null).
   *
   * @throws KDTreeException
   * @throws DimensionException
   */
  @Test
  public void testAddTwo() throws KDTreeException, DimensionException {
    t = new KDTree<>(2);
    List<Point> toAdd = Arrays.asList(new Point(0, 0), new Point(1, 0));
    t.build(toAdd);
    String expected = "[0] 0.0 0.0\n" + "   []\n" + "   [1] 1.0 0.0\n"
        + "      []\n" + "      []";
    String actual = t.treeDiagram();
    assertEquals(expected, actual);
    tearDown();
  }

  /**
   * Tests putting three elements into the KDTree (neither subtree is null).
   *
   * @throws KDTreeException
   * @throws DimensionException
   */
  @Test
  public void testAddThree() throws KDTreeException, DimensionException {
    t = new KDTree<>(2);
    List<Point> toAdd = Arrays.asList(new Point(2, 0), new Point(0, 0),
        new Point(1, 0));
    t.build(toAdd);
    String expected = "[0] 1.0 0.0\n" + "   [1] 0.0 0.0\n" + "      []\n"
        + "      []\n" + "   [1] 2.0 0.0\n" + "      []\n" + "      []";
    String actual = t.treeDiagram();
    assertEquals(expected, actual);
    tearDown();
  }

  /**
   * Tests adding enough poitns into the KDTree that it requires more than two
   * levels.
   *
   * @throws KDTreeException
   * @throws DimensionException
   */
  @Test
  public void testAddMultipleLevels()
      throws KDTreeException, DimensionException {
    t = new KDTree<>(2);
    List<Point> toAdd = Arrays.asList(new Point(3, 2), new Point(2, 0),
        new Point(2, 1), new Point(0, 2), new Point(-1, 0), new Point(0, 1),
        new Point(1, 3));
    t.build(toAdd);
    String expected = "[0] 1.0 3.0\n" + "   [1] 0.0 1.0\n"
        + "      [0] -1.0 0.0\n" + "         []\n" + "         []\n"
        + "      [0] 0.0 2.0\n" + "         []\n" + "         []\n"
        + "   [1] 2.0 1.0\n" + "      [0] 2.0 0.0\n" + "         []\n"
        + "         []\n" + "      [0] 3.0 2.0\n" + "         []\n"
        + "         []";
    String actual = t.treeDiagram();
    assertEquals(expected, actual);
    tearDown();
  }

  /**
   * Tests that the KDTree works with 3D coordinates.
   *
   * @throws KDTreeException
   * @throws DimensionException
   */
  @Test
  public void testAdd3D() throws KDTreeException, DimensionException {
    t = new KDTree<>(3);
    List<Point> toAdd = Arrays.asList(new Point(12, -28, 1),
        new Point(39, 6, 2), new Point(2, 18, -5), new Point(-3, 0, 2),
        new Point(1, 34, 4), new Point(10, 6, -2), new Point(13, -15, 5),
        new Point(9, -9, -6), new Point(23, -1, 11), new Point(6, 28, 11),
        new Point(4, 27, 12), new Point(46, -10, 4), new Point(0, 56, 3),
        new Point(19, -14, 0), new Point(21, -37, -1));
    t.build(toAdd);
    String expected = "[0] 10.0 6.0 -2.0\n" + "   [1] 4.0 27.0 12.0\n"
        + "      [2] 2.0 18.0 -5.0\n" + "         [0] 9.0 -9.0 -6.0\n"
        + "            []\n" + "            []\n"
        + "         [0] -3.0 0.0 2.0\n" + "            []\n"
        + "            []\n" + "      [2] 1.0 34.0 4.0\n"
        + "         [0] 0.0 56.0 3.0\n" + "            []\n"
        + "            []\n" + "         [0] 6.0 28.0 11.0\n"
        + "            []\n" + "            []\n" + "   [1] 19.0 -14.0 0.0\n"
        + "      [2] 12.0 -28.0 1.0\n" + "         [0] 21.0 -37.0 -1.0\n"
        + "            []\n" + "            []\n"
        + "         [0] 13.0 -15.0 5.0\n" + "            []\n"
        + "            []\n" + "      [2] 46.0 -10.0 4.0\n"
        + "         [0] 39.0 6.0 2.0\n" + "            []\n"
        + "            []\n" + "         [0] 23.0 -1.0 11.0\n"
        + "            []\n" + "            []";
    String actual = t.treeDiagram();
    assertEquals(expected, actual);
    tearDown();
  }

  /**
   * Tests that the KDTree works with 1D coordinates (making a BST).
   *
   * @throws KDTreeException
   * @throws DimensionException
   */
  @Test
  public void testAdd1D() throws KDTreeException, DimensionException {
    t = new KDTree<>(1);
    List<Point> toAdd = Arrays.asList(new Point(5), new Point(10), new Point(3),
        new Point(7), new Point(6), new Point(2), new Point(9));
    t.build(toAdd);
    String expected = "[0] 6.0\n" + "   [0] 3.0\n" + "      [0] 2.0\n"
        + "         []\n" + "         []\n" + "      [0] 5.0\n"
        + "         []\n" + "         []\n" + "   [0] 9.0\n" + "      [0] 7.0\n"
        + "         []\n" + "         []\n" + "      [0] 10.0\n"
        + "         []\n" + "         []";
    String actual = t.treeDiagram();
    assertEquals(expected, actual);
    tearDown();
  }

  /**
   * Tests that the KDTree will thrown a exception if the input is empty.
   *
   * @throws KDTreeException
   */
  @Test
  public void testEmpty() throws KDTreeException {
    t = new KDTree<>(1);
    List<Point> toAdd = new ArrayList<>();
    Throwable e = null;
    try {
      t.build(toAdd);
    } catch (Exception ex) {
      e = ex;
    }
    assertTrue(e instanceof KDTreeException);
    tearDown();
  }

  /**
   * Tests how the KDTree is built when some of the points have equal
   * coordinates in one axis.
   *
   * @throws KDTreeException
   * @throws DimensionException
   */
  @Test
  public void testEqualCoordinates()
      throws KDTreeException, DimensionException {
    t = new KDTree<>(1);
    List<Point> toAdd = Arrays.asList(new Point(0), new Point(1), new Point(2),
        new Point(2), new Point(2), new Point(2), new Point(3), new Point(4));
    t.build(toAdd);
    String expected = "[0] 2.0\n" + "   [0] 2.0\n" + "      [0] 1.0\n"
        + "         [0] 0.0\n" + "            []\n" + "            []\n"
        + "         [0] 2.0\n" + "            [0] 2.0\n" + "               []\n"
        + "               []\n" + "            []\n" + "      []\n"
        + "   [0] 3.0\n" + "      []\n" + "      [0] 4.0\n" + "         []\n"
        + "         []";
    String actual = t.treeDiagram();
    assertEquals(expected, actual);
    tearDown();
  }

  /**
   * Tests the neighbors command by comparing it to a version that just sorts a
   * list by distance rather than by using a KDTree. Tests for many k, including
   * 0, 1, and other values less than and greater than the number of elements.
   *
   * @throws KDTreeException
   * @throws DimensionException
   */
  @Test
  public void testRandomNeighbors() throws KDTreeException, DimensionException {
    setUp();
    SearchLocation target = new SearchLocation(randomCoords());
    sortBy(target);
    int k = 0;
    while (k <= numPoints * 2) {
      List<Point> expected = listNeighbors(k);
      List<Point> actual = t.neighbors(k, target);
      assertEquals(expected, actual);
      if (k == 0) {
        k = 1;
      } else {
        k *= 2;
      }
    }
    tearDown();
  }

  /**
   * Tests the radius command by comparing it to a version that just sorts a
   * list by distance rather than by using a KDTree. Tests for many r, including
   * 0, a large enough radius to include everything, and other values in
   * between.
   *
   * @throws KDTreeException
   * @throws DimensionException
   */
  @Test
  public void testRandomRadius() throws KDTreeException, DimensionException {
    setUp();
    SearchLocation target = new SearchLocation(randomCoords());
    sortBy(target);
    double r = 0;
    while (r <= randomLimit * 4) {
      List<Point> expected = listRadius(r, target);
      List<Point> actual = t.radius(r, target);
      assertEquals(expected, actual);
      if (r == 0) {
        r = 0.1;
      } else {
        r *= 1.2;
      }
    }
    tearDown();
  }

  /**
   * Ensures that the neighbors and radius command do not include the target
   * object in the results, if the target happens to be in the tree.
   *
   * @throws KDTreeException
   * @throws DimensionException
   */
  @Test
  public void doesNotReturnEqualToTarget()
      throws KDTreeException, DimensionException {
    Point p1 = new Point(0, 0);
    Point p2 = new Point(1, 0);
    Point p3 = new Point(1, 1);
    Point targetPoint = new Point(0, 0);
    SearchLocation targetSearchLocation = new SearchLocation(0, 0);
    t = new KDTree<>(2);
    List<Point> toAdd = Arrays.asList(p1, p2, p3);
    t.build(toAdd);

    assertEquals(Arrays.asList(p1, p2), t.neighbors(2, targetPoint));
    assertEquals(Arrays.asList(p1, p2), t.neighbors(2, targetSearchLocation));
    assertEquals(Arrays.asList(p2, p3), t.neighbors(2, p1));

    assertEquals(Arrays.asList(p1, p2), t.radius(1.2, targetPoint));
    assertEquals(Arrays.asList(p1, p2), t.radius(1.2, targetSearchLocation));
    assertEquals(Arrays.asList(p2), t.radius(1.2, p1));

    tearDown();
  }

  /**
   * Tests that a negative input for k in neighbors results in an error.
   *
   * @throws KDTreeException
   * @throws DimensionException
   */
  @Test
  public void testNegativeK() throws KDTreeException, DimensionException {
    t = new KDTree<>(2);
    List<Point> toAdd = Arrays.asList(new Point(0, 0), new Point(1, 0),
        new Point(1, 1));
    t.build(toAdd);

    Throwable e = null;
    try {
      t.neighbors(-1, new SearchLocation(0, 0));
    } catch (Exception ex) {
      e = ex;
    }
    assertTrue(e instanceof KDTreeException);

    tearDown();
  }

  /**
   * Tests that a negative input for r in radius results in an error.
   *
   * @throws KDTreeException
   * @throws DimensionException
   */
  @Test
  public void testNegativeR() throws KDTreeException, DimensionException {
    t = new KDTree<>(2);
    List<Point> toAdd = Arrays.asList(new Point(0, 0), new Point(1, 0),
        new Point(1, 1));
    t.build(toAdd);

    Throwable e = null;
    try {
      t.radius(-1, new SearchLocation(0, 0));
    } catch (Exception ex) {
      e = ex;
    }
    assertTrue(e instanceof KDTreeException);

    tearDown();
  }
}