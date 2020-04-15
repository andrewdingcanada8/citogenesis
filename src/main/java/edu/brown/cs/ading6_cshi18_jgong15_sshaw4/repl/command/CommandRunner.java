package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.TypedString;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run.WorldException;

import java.io.PrintWriter;
import java.util.List;

/**
 * Represents one specific input type for a command. A concrete CommandRunner should probably be an
 * inner class of a Command, unless the CommandRunner is needed for multiple commands.
 */
public abstract class CommandRunner {
  /**
   * Constructs a new CommandRunner.
   * @param arguments command arguments (as defined in addOption)
   * @param pw output writer
   * @throws ParseException if there is an error while parsing arguments
   * @throws InvalidInputException if the passed arguments are incorrect
   * @throws WorldException if there is an error in {@link #run} execution
   */
  public abstract void run(List<TypedString> arguments, PrintWriter pw) throws ParseException,
          InvalidInputException,
          WorldException;
}
