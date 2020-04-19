package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.argument;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.InvalidInputException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ArgType;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseProgress;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.TypedString;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.SplitString;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run.WorldException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a specific argument of a command. The arguments to a command are stored in a lazy
 * tree structure, allowing for multiple (or potentially infinite) argument options for a single
 * command. Each argument has an associated type, and each argument has a method returning the
 * "child arguments" (all the arguments that could follow this one).
 * @param <N> the type of argument that can follow this one
 */
public abstract class Argument<N extends Argument<?>> {
  private ArgType<?> argType;

  /**
   * Creates a new Argument to accept a certain type of input.
   * @param argType the type of input this argument represents
   */
  protected Argument(ArgType<?> argType) {
    this.argType = argType;
  }

  /**
   * Returns the ArgType that this Argument uses.
   * @return the ArgType
   */
  public ArgType<?> getArgType() {
    return argType;
  }

  /**
   * The name of this argument.
   * @return the name
   */
  public abstract String getName();

  /**
   * Gets all the Arguments that could come after this one.
   * @return the list of arguments
   */
  public abstract List<N> getNext();

  /**
   * If there is a command associated with this argument, run it. Return true iff a command was run.
   * @param arguments the arguments to give the command
   * @param pw PrinterWriter for output
   * @return whether a command was run
   * @throws ParseException if there is an error in parsing input
   * @throws InvalidInputException if input is incorrect
   * @throws WorldException if there is an error in command runtime
   */
  public abstract boolean run(List<TypedString> arguments, PrintWriter pw) throws ParseException,
          InvalidInputException,
          WorldException;

  /**
   * Attempts to parse the next token off of the remaining string of a ParseProgress as this
   * argument.
   * @param p current ParseProgress in tree
   * @return a new ParseProgress resulting from parsing the token from the beginning of the
   * remaining string
   * @throws ParseException if the remaining string does not start with something of the right type
   */
  public ParseProgress nextToken(ParseProgress p) throws ParseException {
    List<TypedString> tokens = p.getTokens();
    String remaining = p.getRemaining();

    if (!argType.isStartOf(remaining)) {
      throw new ParseException("Cannot interpret \"" + remaining + "\" as starting with type "
          + argType.getName());
    }

    SplitString splitRemaining = argType.split(remaining);
    List<TypedString> newTokens = new ArrayList<>(tokens);
    newTokens.add(new TypedString(splitRemaining.getStart().trim(), argType));
    String newRemaining = splitRemaining.getRest();
    return new ParseProgress(newTokens, newRemaining);
  }
}
