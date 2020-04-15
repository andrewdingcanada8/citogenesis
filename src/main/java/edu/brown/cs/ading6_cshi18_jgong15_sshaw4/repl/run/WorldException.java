package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run;

/**
 * Indicates that something went wrong while running a command or method in a World object.
 */
public class WorldException extends Exception {
  /**
   * Creates a new WorldException.
   * @param message the error message
   */
  public WorldException(String message) {
    super(message);
  }

  /**
   * Creates a new WorldException.
   * @param e an exception whose error message this exception should take on
   */
  public WorldException(Exception e) {
    this(e.getMessage());
  }
}
