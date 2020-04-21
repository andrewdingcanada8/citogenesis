package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.commands;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Graph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.GraphSource;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.arg_types.RawStringArg;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.CommandRunner;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.InvalidInputException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.SimpleCommand;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ArgType;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.TypedString;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run.WorldException;

import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

public class CitationSearchCommand extends SimpleCommand {
  public CitationSearchCommand() {
    super("gen");
    addOption(new GenRunner(),
        new ArgType[]{new RawStringArg()},
        new String[]{"URL"});
  }

  private class GenRunner extends CommandRunner {

    @Override
    public void run(List<TypedString> arguments, PrintWriter pw)
        throws ParseException, InvalidInputException, WorldException {
      String url = new RawStringArg().convert(arguments.get(0));

      /*
      GraphSource<Source, String> webGraphSource; // = new WebGraphSource(_____);
      Graph<Source, String> webGraph; // = new SourcedMemGraph(webGraphSource);
      Source src; // = SourceQuery().query(url);
      Vertex<Source, String> srcVert; // = webGraph.getVertex(src);
      Set<Set<Vertex<Source, String>>> sccs; // = new Tarjan().search(srcVert);
      Set<Vertex<Source, String>> genSoures; // = new FindGenSources.search(sccs);
      *
      */
      pw.write("URL to access: " + url + System.lineSeparator());
      pw.flush();
    }
  }


}
