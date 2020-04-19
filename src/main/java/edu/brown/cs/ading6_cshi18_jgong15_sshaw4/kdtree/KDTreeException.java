package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.kdtree;

/**
 * Signals that something has gone wrong in a KDTree.
 *
 * @author calder
 *
 */
public class KDTreeException extends Exception {
  /**
   * Creates a new KDTreeException.
   *
   * @param message the error message
   */
  public KDTreeException(String message) {
    super(message);
  }
}
