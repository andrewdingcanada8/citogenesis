package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception;

/**
 * Signals an Exception occured internally in the Graph in
 * obtaining Vertex or Edge data/searching.
 */
public class GraphException extends Exception {
  /**
   * returns a new GraphException.
   * @param message error message
   */
  public GraphException(String message) {
    super(message);
  }
}
