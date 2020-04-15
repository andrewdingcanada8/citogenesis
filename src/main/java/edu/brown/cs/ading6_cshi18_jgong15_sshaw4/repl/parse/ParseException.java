package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse;

/**
 * Signals that a string cannot be parsed as a certain type.
 *
 * @author calder
 *
 */
public class ParseException extends Exception {
  /**
   * Creates a new ParseException.
   *
   * @param message the error message
   */
  public ParseException(String message) {
    super(message);
  }
}
