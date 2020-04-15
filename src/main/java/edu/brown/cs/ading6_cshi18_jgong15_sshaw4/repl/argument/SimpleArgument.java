package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.argument;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ArgType;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.CommandRunner;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.InvalidInputException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.TypedString;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run.WorldException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * A non-root Argument that stores all its child Arguments in a List.
 */
public class SimpleArgument extends ListArgument<SimpleArgument> {
  private String name;
  private CommandRunner cr;

  /**
   * Create a SimpleArgument that can run a command (if this argument is the last one) but can
   * also be followed by other arguments (if this argument is not the last one).
   * @param name the name of the argument
   * @param argType the type of input this argument represents
   * @param cr the CommandRunner to run, if this is the last argument
   * @param next the arguments that could come next, if this is not the last argument
   */
  public SimpleArgument(String name, ArgType<?> argType, CommandRunner cr,
                        List<SimpleArgument> next) {
    super(argType, next);
    this.name = name;
    this.cr = cr;
  }

  /**
   * Creates a SimpleArgument that cannot run a command and must be followed by other arguments.
   * @param name the name of the argument
   * @param argType the type of input this argument represents
   * @param next the arguments that could come next
   */
  public SimpleArgument(String name, ArgType<?> argType, List<SimpleArgument> next) {
    this(name, argType, null, next);
  }

  /**
   * @param name the name of the argument
   * @param argType the type of input this argument represents
   * @param cr the CommandRunner to run
   */
  public SimpleArgument(String name, ArgType<?> argType, CommandRunner cr) {
    this(name, argType, cr, new ArrayList<>());
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean run(List<TypedString> arguments, PrintWriter pw) throws ParseException,
          InvalidInputException,
          WorldException {
    if (cr == null) {
      return false;
    } else {
      cr.run(arguments, pw);
      return true;
    }
  }

  @Override
  public String toString() {
    return name;
  }
}
