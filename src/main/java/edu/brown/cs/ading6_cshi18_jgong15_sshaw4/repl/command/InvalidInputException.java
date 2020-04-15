package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command;

/**
 * Signals that the input to a command is invalid.
 *
 * @author calder
 *
 */
public class InvalidInputException extends Exception {
  /**
   * Creates a new InvalidInputException.
   *
   * @param message the error message
   */
  public InvalidInputException(String message) {
    super(message);
  }
}
