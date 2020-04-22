package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.commands;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Wiki;
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
      WikiTestUtils
      Wiki wiki = new Wiki();

      pw.write("Set wiki to " + url);
      pw.flush();
    }
  }
}
