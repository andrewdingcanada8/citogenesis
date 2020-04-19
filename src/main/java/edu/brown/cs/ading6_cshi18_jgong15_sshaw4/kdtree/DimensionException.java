package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.kdtree;

/**
 * Signals that something is wrong with the dimensions of Locatables.
 * @author calder
 *
 */
public class DimensionException extends Exception {
  /**
   * Create a new DimensionException.
   * @param message the error message
   */
  public DimensionException(String message) {
    super(message);
  }
}
