package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.database;

import java.util.List;

/**
 * Used for querying databases with no input. Returns a list of objects of type R representing
 * things from the database.
 *
 * @param <R> The type of object in the list returned by the query
 */
public interface NoInputListDatabaseProxy<R> extends NoInputDatabaseProxy<List<R>> { }
