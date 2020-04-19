package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.kdtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Represents objects in k-dimensional space using a k-d tree.
 *
 * @author calder
 *
 * @param <T> the type of object to store
 */
public class KDTree<T extends Locatable> {
  private int dimension;
  private T value;
  private int depth;
  private int axis;
  private KDTree<T> lessOrEqual;
  private KDTree<T> greater;
  private int size;

  /**
   * Creates a new KDTree.
   *
   * @param dimension the dimension of the space that the elements are in
   * @param depth     the depth at this level of the tree (root = 0)
   */
  public KDTree(int dimension, int depth) {
    this.dimension = dimension;
    this.depth = depth;
    this.axis = depth % dimension;
    this.value = null;
    this.lessOrEqual = null;
    this.greater = null;
    this.size = 0;
  }

  /**
   * Creates a new KDTree.
   *
   * @param dimension the dimension of the space that the elements are in
   */
  public KDTree(int dimension) {
    this(dimension, 0);
  }

  /**
   * Returns the number of elements in the KDTree.
   *
   * @return the size
   */
  public int size() {
    return size;
  }

  /**
   * Accessor for the left subtree lessOrEqual.
   *
   * @return the left subtree
   */
  public KDTree<T> getLessOrEqual() {
    return lessOrEqual;
  }

  /**
   * Accessor for the right subtree greater.
   *
   * @return the right subtree
   */
  public KDTree<T> getGreater() {
    return greater;
  }

  /**
   * Adds a list of values to the tree. Sets the value of this node to be an
   * element at (or near) the middle, and puts the elements less than and
   * greater than it in the subtrees.
   *
   * @param values the values to add to the tree
   * @throws KDTreeException    if something goes wrong in the KDTree
   * @throws DimensionException if something goes wrong with the dimensions
   */
  public void build(List<T> values) throws KDTreeException, DimensionException {
    int numValues = values.size();
    if (numValues == 0) {
      throw new KDTreeException("The input to add must not be of size 0.");
    } else {
      size = numValues;
    }

    // On the highest-level call to add (containing all the elements), check
    // that they are all the same dimension
    if (depth == 0 && numValues > 1) {
      for (int i = 1; i < numValues; i++) {
        if (values.get(i).getDimension() != dimension) {
          throw new DimensionException(
              "Every element in the input to add must have the same dimension as the KDTree.");
        }
      }
    }

    AxisComparator axisComparator = new AxisComparator(axis);
    // Need to sort it at each level because each level uses a different axis
    Collections.sort(values, axisComparator);
    int centerIndex = (numValues - 1) / 2;
    // For an odd number, this is the middle element
    // For an even number, this is the left of the two middle elements
    T center = values.get(centerIndex);
    // In case there are multiple elements with this position on this axis, find
    // the index of the one farthest into the list, so all the equal ones will
    // be grouped together
    int cutoffIndex = centerIndex;
    while (cutoffIndex + 1 < numValues
        && axisComparator.compare(center, values.get(cutoffIndex + 1)) == 0) {
      cutoffIndex++;
    }
    // At this point cutoffIndex should be the index of the last element in the
    // list whose coordinate on this axis is the same as that of center

    T cutoff = values.get(cutoffIndex);
    List<T> lessOrEqualHalf = values.subList(0, cutoffIndex);
    List<T> greaterHalf = values.subList(cutoffIndex + 1, numValues);

    value = cutoff;
    if (lessOrEqualHalf.size() > 0) {
      lessOrEqual = new KDTree<T>(dimension, depth + 1);
      lessOrEqual.build(lessOrEqualHalf);
    }
    if (greaterHalf.size() > 0) {
      greater = new KDTree<T>(dimension, depth + 1);
      greater.build(greaterHalf);
    }
  }

  /**
   * Finds the k nearest neighbors to a target Locatable (that are not the same
   * object as the target).
    *
   * @param k      the number of neighbors to find
   * @param target the element to search around
   * @return the k neighbors nearest to the target (that are not the same object
   *         as the target)
   * @throws DimensionException if something goes wrong with the dimensions
   * @throws KDTreeException    if something goes wrong in the KDTree
   */
  public List<T> neighbors(int k, Locatable target)
      throws DimensionException, KDTreeException {
    if (k < 0) {
      throw new KDTreeException("k must be nonnegative.");
    }

    if (k == 0) {
      List<T> empty = new ArrayList<>();
      return empty;
    }

    PriorityQueue<T> nearest = new PriorityQueue<>(k,
        new DistanceComparator(target, false));
    formNeighborsQueue(k, target, nearest);

    List<T> nearestList = new ArrayList<T>(nearest);
    Collections.sort(nearestList, new DistanceComparator(target, true));
    return nearestList;
  }

  private void formNeighborsQueue(int k, Locatable target,
      PriorityQueue<T> nearest) throws DimensionException, KDTreeException {

    if (target.getDimension() != dimension) {
      throw new DimensionException(
          "The dimension of the target point must be the same as the dimension of the KDTree.");
    }

    if (value != target) { // Don't want to include the target itself among
                           // neighbors
      if (nearest.size() < k) { // If there aren't enough elements in nearest,
                                // don't need to check the distance
        nearest.add(value);
      } else {
        double distanceFromTarget = value.distanceTo(target);
        double farthestNeighborDistance = nearest.peek().distanceTo(target);
        if (distanceFromTarget < farthestNeighborDistance) {
          nearest.poll();
          nearest.add(value);
        }
      }
    }

    boolean recurOnBoth = nearest.size() < k;
    if (!recurOnBoth) {
      double farthestNeighborDistance = nearest.peek().distanceTo(target);
      double axisDistance = value.axisDistanceTo(target, axis);
      recurOnBoth = farthestNeighborDistance >= axisDistance;
    }

    if (recurOnBoth) {
      if (lessOrEqual != null) {
        lessOrEqual.formNeighborsQueue(k, target, nearest);
      }
      if (greater != null) {
        greater.formNeighborsQueue(k, target, nearest);
      }
    } else {
      AxisComparator ac = new AxisComparator(axis);
      int comparison = ac.compare(target, value);
      if (comparison > 0 && greater != null) {
        greater.formNeighborsQueue(k, target, nearest);
      } else if (lessOrEqual != null) {
        lessOrEqual.formNeighborsQueue(k, target, nearest);
      }
    }
  }

  /**
   * Finds all elements with distance r or less of a target Locatable (that are
   * not the same object as the target).
   *
   * @param r      the radius to search within
   * @param target the element to search around
   * @return all the elements within radius r of the target (that are not the
   *         same object as the target)
   * @throws DimensionException if something goes wrong with the dimensions
   * @throws KDTreeException    if something goes wrong in the tree
   */
  public List<T> radius(double r, Locatable target)
      throws DimensionException, KDTreeException {
    if (r < 0) {
      throw new KDTreeException("r must be nonnegative.");
    }

    List<T> nearest = new ArrayList<>();
    formRadiusList(r, target, nearest);
    Collections.sort(nearest, new DistanceComparator(target, true));
    return nearest;
  }

  private void formRadiusList(double r, Locatable target, List<T> nearest)
      throws DimensionException {
    if (target.getDimension() != dimension) {
      throw new DimensionException(
          "The dimension of the target point must be the same as the dimension of the KDTree.");
    }

    if (value != target) { // Don't want to include the target itself among neighbors
      double distanceFromTarget = value.distanceTo(target);
      if (distanceFromTarget <= r) {
        nearest.add(value);
      }
    }

    double axisDistance = value.axisDistanceTo(target, axis);
    if (r >= axisDistance) {
      if (lessOrEqual != null) {
        lessOrEqual.formRadiusList(r, target, nearest);
      }
      if (greater != null) {
        greater.formRadiusList(r, target, nearest);
      }
    } else {
      AxisComparator ac = new AxisComparator(axis);
      int comparison = ac.compare(target, value);
      if (comparison > 0 && greater != null) {
        greater.formRadiusList(r, target, nearest);
      } else if (lessOrEqual != null) {
        lessOrEqual.formRadiusList(r, target, nearest);
      }
    }

  }

  @Override
  public String toString() {
    if (value == null) {
      return "?";
    } else {
      String lessOrEqualString = "_";
      if (lessOrEqual != null) {
        StringBuilder sb = new StringBuilder("(");
        sb.append(lessOrEqual);
        sb.append(")");
        lessOrEqualString = sb.toString();
      }
      String greaterString = "_";
      if (greater != null) {
        StringBuilder sb = new StringBuilder("(");
        sb.append(greater);
        sb.append(")");
        greaterString = sb.toString();
      }
      StringBuilder sb = new StringBuilder("[");
      sb.append(axis);
      sb.append("] ");
      sb.append(value);
      sb.append(" ");
      sb.append(lessOrEqualString);
      sb.append(" ");
      sb.append(greaterString);
      return sb.toString();
    }
  }

  /**
   * Creates a visualization of the tree (mainly used for testing).
   *
   * @return the visualization
   */
  public String treeDiagram() {
    if (value == null) {
      return "?";
    } else {
      String lessOrEqualString = "[]";
      if (lessOrEqual != null) {
        lessOrEqualString = lessOrEqual.treeDiagram();
      }
      String greaterString = "[]";
      if (greater != null) {
        greaterString = greater.treeDiagram();
      }

      StringBuilder sb = new StringBuilder();
      sb.append("[");
      sb.append(axis);
      sb.append("] ");
      sb.append(value);
      sb.append("\n");
      sb.append("   ".repeat(depth + 1));
      sb.append(lessOrEqualString);
      sb.append("\n");
      sb.append("   ".repeat(depth + 1));
      sb.append(greaterString);
      return sb.toString();
    }
  }
}
