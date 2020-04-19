package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.kdtree;

/**
 * Represents a point to be used as the target in KDTree search.
 *
 * @author calder
 *
 */
public class SearchLocation implements Locatable {
  private final double[] coordinates;
  private final int dimension;

  /**
   * Creates a new SearchLocation with the input coordinates.
   *
   * @param coordinates the coordinates of this location
   */
  public SearchLocation(double... coordinates) {
    this.coordinates = coordinates;
    this.dimension = coordinates.length;
  }

  @Override
  public int getDimension() {
    return dimension;
  }

  @Override
  public double getCoordinate(int d) throws DimensionException {
    if (d >= dimension) {
      throw new DimensionException("Cannot find coordinate " + d
          + " of a point in " + dimension + " dimensions.");
    }
    return coordinates[d];
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < dimension; i++) {
      sb.append(coordinates[i]);
      if (i < dimension - 1) {
        sb.append(" ");
      }
    }
    return sb.toString();
  }
}
