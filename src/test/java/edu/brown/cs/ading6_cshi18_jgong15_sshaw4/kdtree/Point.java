package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.kdtree;

import java.util.Arrays;
import java.util.Objects;

public class Point implements Locatable {
  private double[] coordinates;
  private int dimension;

  public Point(double... coordinates) {
    this.coordinates = coordinates;
    this.dimension = coordinates.length;
  }

  @Override
  public int getDimension() {
    return dimension;
  }

  @Override
  public double getCoordinate(int axis) throws DimensionException {
    return coordinates[axis];
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Point point = (Point) o;
    return dimension == point.dimension &&
        Arrays.equals(coordinates, point.coordinates);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(dimension);
    result = 31 * result + Arrays.hashCode(coordinates);
    return result;
  }
}
