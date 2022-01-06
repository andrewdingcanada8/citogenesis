package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.commands;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.graph.AsyncSearchWebGraph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.ops.GeneratingSourceFinder;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.async.AsyncSourceQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.segment.Tarjan;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.arg_types.IntArg;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.arg_types.QuotedStringArg;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.CommandRunner;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.InvalidInputException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.SimpleCommand;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ArgType;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.TypedString;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run.WorldException;

import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

public class CitationSearchCommand extends SimpleCommand {
  public CitationSearchCommand() {
    super("gen");
    addOption(new GenRunner(),
        new ArgType[]{new QuotedStringArg(), new IntArg(), new QuotedStringArg()},
        new String[]{"url", "timeout", "keywords"});
  }

  private class GenRunner extends CommandRunner {

    @Override
    public void run(List<TypedString> arguments, PrintWriter pw)
        throws ParseException, InvalidInputException, WorldException {
      String url = new QuotedStringArg().convert(arguments.get(0));
      int timeout = new IntArg().convert(arguments.get(1));
      String keywords = new QuotedStringArg().convert(arguments.get(2));

      Query<String, CompletableFuture<Source>> sourceQuery = new AsyncSourceQuery(timeout);
      Source citedSrc;
      try {
        citedSrc = sourceQuery.query(url).join();
      } catch (Exception e) {
        throw new WorldException("invalid URL");
      }
      AsyncSearchWebGraph graph = new AsyncSearchWebGraph(citedSrc, sourceQuery, keywords);
      List<Vertex<Source, String>> gens;
      try {
        graph.load();
        Vertex<Source, String> citeVert = graph.getHead();
        List<Set<Vertex<Source, String>>> comps = new Tarjan().search(citeVert);
        comps.stream()
            .flatMap(set -> set.stream())
            .map(Vertex :: getVal)
            .forEach(Source::queryTimestamp);
        gens = comps.stream()
            .map(comp -> {
              try {
                return new GeneratingSourceFinder().search(comp);
              } catch (GraphException e) {
                return null;
              }
            })
            .collect(Collectors.toList());
      } catch (GraphException e) {
        throw new WorldException(e.getMessage());
      }
      StringBuilder out = new StringBuilder();
      gens.stream()
          .filter(Objects::nonNull)
          .map(Vertex::getVal)
          .forEach(src -> out.append(src.getURL() + System.lineSeparator()));
      pw.write(out.toString());
      pw.flush();
    }
  }


}
