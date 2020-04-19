package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception;

/**
 * Signals an error in {@link edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.GraphSource}.
 */
public class SourceParseException extends Exception {
  /**
   * Creates a new SourceParseException.
   * @param message error message
   */
  public SourceParseException(String message) {
    super(message);
  }
}
