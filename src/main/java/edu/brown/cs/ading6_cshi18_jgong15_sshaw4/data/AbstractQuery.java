package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;

public interface AbstractQuery<T, W> {
  W query(T input) throws QueryException;
}
