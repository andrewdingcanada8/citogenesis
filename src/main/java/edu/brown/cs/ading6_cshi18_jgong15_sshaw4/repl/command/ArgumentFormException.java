package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command;

/**
 * Indicates that something is badly-formed about an argument tree.
 */
public class ArgumentFormException extends Exception {
  /**
   * Constructs a new ArgumentFormException.
   * @param message error message
   */
  public ArgumentFormException(String message) {
    super("Issue with this command's argument structure: " + message);
  }
}
