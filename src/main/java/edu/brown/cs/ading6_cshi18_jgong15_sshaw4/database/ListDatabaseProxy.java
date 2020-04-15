package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.database;

import java.util.List;

/**
 * Used for querying databases with input. Inputs an object of type Q to represent the parameters
 * of the query, and returns a list of objects of type R representing things from the database
 * that match those parameters.
 *
 * @param <Q> The type of object input into the query
 * @param <R> The type of object returned by the query
 */
public interface ListDatabaseProxy<Q, R> extends DatabaseProxy<Q, List<R>> { }
