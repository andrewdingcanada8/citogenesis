package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.kdtree;

import java.util.Comparator;

/**
 * Used for comparing two Locatables along a certain axis.
 *
 * @author calder
 *
 */
public class AxisComparator implements Comparator<Locatable> {
  private int axis;

  /**
   * Creates an AxisComparator for a particular axis.
   *
   * @param axis the index of the axis to compare on
   */
  public AxisComparator(int axis) {
    this.axis = axis;
  }

  @Override
  public int compare(Locatable l1, Locatable l2) {
    int dim1 = l1.getDimension();
    int dim2 = l2.getDimension();

    try {
      if (dim1 != dim2) {
        throw new DimensionException(
            "Cannot compare locations with different numbers of dimensions.");
      }
      if (axis > dim1 || axis > dim2) {
        throw new DimensionException(
            "The axis index is larger than at least one of the objects' dimensions.");
      }
    } catch (DimensionException e) { // Not sure how to deal with this issue,
                                     // especially when I will ensure that the
                                     // inputs to this comparator always have
                                     // the same dimension.
      if (dim1 < dim2) {
        return -1;
      } else {
        return 1;
      }
    }

    try {
      double c1 = l1.getCoordinate(axis);
      double c2 = l2.getCoordinate(axis);
      if (c1 < c2) {
        return -1;
      } else if (c1 == c2) {
        return 0;
      } else {
        return 1;
      }
    } catch (DimensionException d) {
      return 1;
    }
  }
}
