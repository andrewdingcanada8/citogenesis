package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.database;

/**
 * Signals that something went wrong when querying a DatabaseProxy.
 */
public class DatabaseProxyException extends Exception {
  /**
   * Creates a new DatabaseProxyException.
   * @param message the error message
   */
  public DatabaseProxyException(String message) {
    super(message);
  }
}
