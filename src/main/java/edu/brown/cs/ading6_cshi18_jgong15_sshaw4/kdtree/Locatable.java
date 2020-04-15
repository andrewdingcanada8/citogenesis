package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.kdtree;

/**
 * Represents an object that can be located with Cartesian coordinates.
 *
 * @author calder
 *
 */
public interface Locatable {
  /**
   * Gets the number of dimensions of this Locatable.
   *
   * @return the numbero f dimensions
   */
  int getDimension();

  /**
   * Gets one coordinate.
   *
   * @param axis the index of the coordinate to get (e.g. 0 = x, 1 = y)
   * @return the value of the coordinate
   * @throws DimensionException if the number input is too large
   */
  double getCoordinate(int axis) throws DimensionException;

  /**
   * Finds the Euclidean distance to another Locatable.
   *
   * @param other the Locatable to which you want to find the distance
   * @return the distance to that Locatable
   * @throws DimensionException when this and other have different numbers of
   *                            dimensions
   */
  default double distanceTo(Locatable other) throws DimensionException {
    int d1 = this.getDimension();
    int d2 = other.getDimension();
    if (d1 != d2) {
      throw new DimensionException(
          "Cannot compare locations with different numbers of dimensions.");
    }

    double sum = 0;
    for (int i = 0; i < d1; i++) {
      double w1 = this.getCoordinate(i);
      double w2 = other.getCoordinate(i);
      double dw = w1 - w2;
      sum += dw * dw;
    }

    return Math.sqrt(sum);
  }

  /**
   * Finds the distance to another Locatable along one axis.
   *
   * @param other the Locatable to which you want to find the distance
   * @param axis  the axis with which to calculate the distance
   * @return the distance
   * @throws DimensionException if axis is too large or if the Locatables have
   *                            different numbers of dimensions
   */
  default double axisDistanceTo(Locatable other, int axis)
      throws DimensionException {
    int d1 = this.getDimension();
    int d2 = other.getDimension();
    if (d1 != d2) {
      throw new DimensionException(
          "Cannot compare locations with different numbers of dimensions.");
    }
    if (axis > d1 || axis > d2) {
      throw new DimensionException(
          "The axis index is larger than at least one of the objects' dimensions.");
    }

    return Math.abs(this.getCoordinate(axis) - other.getCoordinate(axis));
  }
}
