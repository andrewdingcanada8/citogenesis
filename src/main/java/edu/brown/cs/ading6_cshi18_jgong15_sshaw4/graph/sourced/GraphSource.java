package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.SourceParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.forgetful.SourcedForgetfulGraph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.forgetful.SourcedForgetfulVertex;

import java.util.Set;

/**
 * The heart of the SourcedGraph implementation, and the interface that must be
 * implemented by the user to leverage the {@link SourcedForgetfulGraph}, {@link SourcedEdge},
 * and {@link SourcedForgetfulVertex} classes. Defines how to obtain vertex edges based on
 * a given vertex, presumably drawn from an external source (Database, CSV, Web API, etc.).
 * classes.
 * @param <T> Value type stored in Graph {@link SourcedForgetfulVertex}
 * @param <W> Value type stored in Graph {@link SourcedEdge}
 */
public interface GraphSource<T, W> {

  /**
   * Defines which edges are connected to a given vertex in a SourceGraph.
   * @param val {@link SourcedForgetfulVertex} from which to find Graph edges.
   * @return Set of {@link Edge} (typically {@link SourcedEdge} connected to val.
   * @throws SourceParseException Exception to throw in case there are is an Exception with
   * the external source (IOException, SQLException, etc)
   */
  Set<Edge<T, W>> getEdges(SourcedVertex<T, W> val) throws SourceParseException;

}
