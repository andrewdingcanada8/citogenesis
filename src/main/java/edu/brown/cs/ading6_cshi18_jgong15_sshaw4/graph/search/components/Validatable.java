package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.components;

/**
 * Indicates a specific Vertex value can be considered a valid minimum
 * for {@link GeneratingVertexFinder}.
 */
public interface Validatable {
  /**
   * Returns if value is valid.
   *
   * @return true if valid, false if not
   */
  boolean isValid();
}
