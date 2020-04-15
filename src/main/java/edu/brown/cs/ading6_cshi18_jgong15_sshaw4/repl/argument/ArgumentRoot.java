package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.argument;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.InvalidInputException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ArgType;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseProgress;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.TypedString;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run.WorldException;

import java.io.PrintWriter;
import java.util.List;

/**
 * Represents the root of a command's argument tree.
 * @param <N> The type of all options for the first argument.
 */
public class ArgumentRoot<N extends Argument<?>> extends ListArgument<N> {
  /**
   * Constructs a new ArgumentRoot.
   * @param options a list of Argument branches
   */
  public ArgumentRoot(List<N> options) {
    super(null, options);
  }

  @Override
  public ArgType<?> getArgType() {
    throw new UnsupportedOperationException("The argument root does not have a type.");
  }

  @Override
  public String getName() {
    throw new UnsupportedOperationException("The argument root does not have a name.");
  }

  @Override
  public boolean run(List<TypedString> arguments, PrintWriter pw) throws ParseException,
          InvalidInputException, WorldException {
    return false;
  }

  @Override
  public ParseProgress nextToken(ParseProgress p) throws ParseException {
    return p;
  }
}
