package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.kdtree;

import java.util.Comparator;

/**
 * Compares two Locatables to see which is closer/farther from a third reference
 * Locatable.
 *
 * @author calder
 *
 */
public class DistanceComparator implements Comparator<Locatable> {
  private Locatable target;
  private int dimension;
  private boolean ascending;

  /**
   * Creates a new DistanceComparator.
   *
   * @param target    the reference Locatable from which to measure the distance
   * @param ascending whether to put farther things first (true) or closer
   *                  things first (false)
   */
  public DistanceComparator(Locatable target, boolean ascending) {
    this.target = target;
    this.dimension = target.getDimension();
    this.ascending = ascending;
  }

  @Override
  public int compare(Locatable l1, Locatable l2) {
    int dim1 = l1.getDimension();
    int dim2 = l2.getDimension();

    try {
      if (dim1 != dim2 || dim1 != dimension || dim2 != dimension) {
        throw new DimensionException(
            "Cannot compare locations with different numbers of dimensions.");
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
      double dist1 = target.distanceTo(l1);
      double dist2 = target.distanceTo(l2);

      if (dist1 < dist2) {
        if (ascending) {
          return -1;
        } else {
          return 1;
        }
      } else if (dist1 == dist2) {
        return 0;
      } else {
        if (ascending) {
          return 1;
        } else {
          return -1;
        }
      }
    } catch (DimensionException e) {
      return 1; // uhhh
    }
  }
}
