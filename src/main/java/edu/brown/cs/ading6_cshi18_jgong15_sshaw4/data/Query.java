package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;

/**
 * Used for querying databases with input. Inputs an object of type Q to represent the parameters
 * of the query, and returns an object of type R representing a thing or things from the
 * database that match those parameters.
 *
 * @param <Q> The type of object input into the query
 * @param <R> The type of object returned by the query
 * @param <D> DataSource type
 * @param <T> Type of query object
 * @param <W> Type of result object
 * @param <M> Type of source object
 */
public abstract class Query<Q, R, D extends DataSource<T, W, M>, T, W, M>
    implements AbstractQuery<Q, R> {
  private D source;
  public Query(D source) {
    this.source = source;
  }

  protected abstract T getQuery(Q input, M src);

  protected abstract R processResult(W result) throws QueryException;

  /**
   * @param input the parameters of the query
   * @return an object representing a thing or things from the database that match those parameters
   * @throws QueryException if something goes wrong accessing the database or if nothing
   * can be returned
   */
  @Override
  public R query(Q input) throws QueryException {
    T query = getQuery(input, source.getSource());
    try {
      W result = source.runQuery(query);
      return processResult(result);
    } catch (Exception e) {
      throw new QueryException("Data proxy exception: " + e.getMessage());
    }
  }
}
