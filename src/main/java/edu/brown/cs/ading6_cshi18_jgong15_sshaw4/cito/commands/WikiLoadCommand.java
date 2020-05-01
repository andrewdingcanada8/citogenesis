package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.commands;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki.Wiki;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.WikiQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.sync.CheckConnectionQuery;
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

public class WikiLoadCommand extends SimpleCommand {
  public WikiLoadCommand() {
    super("wiki");
    addOption(new WikiRunner(),
        new ArgType[]{new RawStringArg()},
        new String[]{"URL"});
  }

  public static final int WIKI_TIMEOUT = 30;

  private class WikiRunner extends CommandRunner {

    /**
     * Constructs a new CommandRunner.
     *
     * @param arguments command arguments (as defined in addOption)
     * @param pw        output writer
     * @throws ParseException        if there is an error while parsing arguments
     * @throws InvalidInputException if the passed arguments are incorrect
     * @throws WorldException        if there is an error in {@link #run} execution
     */
    @Override
    public void run(List<TypedString> arguments, PrintWriter pw)
        throws ParseException, InvalidInputException, WorldException {
      String url = new RawStringArg().convert(arguments.get(0));
      checkURL(url);
      try {
        Wiki wiki = new WikiQuery(WIKI_TIMEOUT).query(url);
        // TODO: set wiki
        pw.write("Set wiki to " + url);
        pw.flush();
      } catch (QueryException e) {
        throw new WorldException("ERROR: cannot access " + url);
      }
    }

    public boolean checkURL(String url) {
      try {
        return new CheckConnectionQuery(5).query(url);
      } catch (QueryException e) {
        return false;
      }
    }
  }
}
