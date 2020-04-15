package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.database;

/**
 * Used for querying databases with no input. Returns an object of type R representing a thing or
 * things from the database.
 *
 * @param <R> The type of object returned by the query
 */
public interface NoInputDatabaseProxy<R> {
  /**
   * @return an object representing a thing or things from the database
   * @throws DatabaseProxyException if something goes wrong accessing the database or if nothing
   * can be returned
   */
  R query() throws DatabaseProxyException;
}
