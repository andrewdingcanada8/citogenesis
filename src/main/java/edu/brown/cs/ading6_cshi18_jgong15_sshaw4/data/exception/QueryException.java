package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception;

/**
 * Signals that something went wrong when querying a DatabaseProxy.
 */
public class QueryException extends Exception {
  /**
   * Creates a new DatabaseProxyException.
   * @param message the error message
   */
  public QueryException(String message) {
    super(message);
  }
}
