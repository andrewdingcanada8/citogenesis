package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.commands.CitationSearchCommand;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.commands.DownloadWikiCommand;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.commands.WikiLoadCommand;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki.Wiki;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.arg_types.DoubleArg;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.arg_types.NaturalNumberArg;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.CommandRunner;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.InvalidInputException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.SimpleCommand;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ArgType;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.TypedString;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run.World;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run.WorldException;

import java.io.PrintWriter;
import java.util.List;

public final class CitoWorld extends World {
  private static CitoWorld citoWorld;

  private static final int DEFAULT_TIME_OUT = 60;
  private static final int DEFAULT_DEPTH = 5;
  private static final double DEFAULT_THRESHOLD = 0.200;


  private Wiki wiki;
  private int globalDepth;
  private double globalThresh;
  private int globalTimeout;

  public static CitoWorld getInstance() {
    if (citoWorld == null) {
      citoWorld = new CitoWorld();
    }
    return citoWorld;
  }

  private CitoWorld() {
    super();
    globalDepth = DEFAULT_DEPTH;
    globalThresh = DEFAULT_THRESHOLD;
    globalTimeout = DEFAULT_TIME_OUT;
    addCommands(new CitationSearchCommand(), new WikiLoadCommand(), new SetParamsCommand(), new DownloadWikiCommand());
  }

  public void setWiki(Wiki wiki) {
    this.wiki = wiki;
  }

  public int getDepth() {
    return globalDepth;
  }

  public double getThresh() {
    return globalThresh;
  }

  public int getTimeout() {
    return globalTimeout;
  }

  public void setDepth(int d) {
    globalDepth = d;
  }

  public void setThresh(double th) {
    globalThresh = th;
  }

  public void setTimeout(int ti) {
    globalTimeout = ti;
  }

  private class SetParamsCommand extends SimpleCommand {

    protected SetParamsCommand() {
      super("params");
      addOption(new ParamsRunner(),
          new ArgType[]{new NaturalNumberArg(), new DoubleArg(), new NaturalNumberArg()},
          new String[]{"depth", "relevance threshold", "timeout in seconds"});
    }

    private class ParamsRunner extends CommandRunner {

      @Override
      public void run(List<TypedString> arguments, PrintWriter pw)
          throws ParseException, InvalidInputException, WorldException {
        int dp = new NaturalNumberArg().convert(arguments.get(0));
        double th = new DoubleArg().convert(arguments.get(1));
        int ti = new NaturalNumberArg().convert(arguments.get(2));

        // basic argument correctness checking
        if (dp < 0) {
          throw new InvalidInputException("depth must be a non-negative integer");
        }
        if (th < 0.0 || th > 1.0) {
          throw new InvalidInputException("threshold must be between 0 and 1, inclusive");
        }
        if (ti <= 0) {
          throw new InvalidInputException("timout must be a positive integer");
        }

        // update in CitoWorld and report out
        CitoWorld.getInstance().setDepth(dp);
        CitoWorld.getInstance().setTimeout(ti);
        CitoWorld.getInstance().setThresh(th);
        pw.write("Set: depth="
            + dp + ", thresh="
            + th + ", timeout="
            + ti + System.lineSeparator());
        pw.flush();
      }
    }
  }


}
