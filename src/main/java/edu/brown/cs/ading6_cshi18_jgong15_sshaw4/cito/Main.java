package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run.REPL;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run.World;

import java.io.PrintWriter;

public final class Main {
  private Main() { }

  public static void main(String[] args) {
    REPL repl = new REPL(new PrintWriter(System.out), CitoWorld.getInstance());
    repl.run();
  }


  // Seiji's TODO:
  // caching on lemmatization process
  // implement 'second pass' timestamp when it becomes relevant (when identifying generators)
  // graph synchronization for url-node differences
  // multi-threading bfs


  // implement citation text relevancy (instead of source-source relevancy)

}
