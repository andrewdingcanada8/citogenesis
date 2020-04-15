package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.database;

/**
 * Used for querying databases with input. Inputs an object of type Q to represent the parameters
 * of the query, and returns an object of type R representing a thing or things from the
 * database that match those parameters.
 *
 * @param <Q> The type of object input into the query
 * @param <R> The type of object returned by the query
 */
public interface DatabaseProxy<Q, R> {
  /**
   * @param input the parameters of the query
   * @return an object representing a thing or things from the database that match those parameters
   * @throws DatabaseProxyException if something goes wrong accessing the database or if nothing
   * can be returned
   */
  R query(Q input) throws DatabaseProxyException;
}
