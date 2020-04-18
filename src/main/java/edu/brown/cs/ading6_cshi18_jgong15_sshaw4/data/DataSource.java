package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.DataSourceException;

public interface DataSource<T, W, M> {
  /**
   * Run query with specified input.
   * @param queryInput query input
   * @return output from query run (ex: ResultSet for sqlite).
   */
  W runQuery(T queryInput) throws DataSourceException;

  M getSource();
}
